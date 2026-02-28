package com.hongguoyan.module.biz.cache.adjustmentadmit;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.hongguoyan.module.biz.cache.CacheNames;
import com.hongguoyan.module.biz.controller.app.adjustmentadmit.vo.AppAdjustmentAdmitListItemRespVO;
import com.hongguoyan.module.biz.controller.app.adjustmentadmit.vo.AppAdjustmentAdmitListReqVO;
import com.hongguoyan.module.biz.dal.dataobject.adjustmentadmit.AdjustmentAdmitDO;
import com.hongguoyan.module.biz.dal.dataobject.nationalscore.NationalScoreDO;
import com.hongguoyan.module.biz.dal.dataobject.school.SchoolDO;
import com.hongguoyan.module.biz.dal.mysql.adjustmentadmit.AdjustmentAdmitMapper;
import com.hongguoyan.module.biz.dal.mysql.school.SchoolMapper;
import com.hongguoyan.module.biz.service.recommend.NationalLineEligibilityService;
import jakarta.annotation.Resource;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 录取名单-缓存。
 */
@Service
@Validated
public class AdjustmentAdmitCache {

    @Resource
    private AdjustmentAdmitMapper adjustmentAdmitMapper;
    @Resource
    private SchoolMapper schoolMapper;
    @Resource
    private NationalLineEligibilityService nationalLineEligibilityService;

    @Cacheable(cacheNames = CacheNames.ADJUSTMENT_ADMIT_LIST,
            key = "'year:' + #reqVO.year"
                    + " + ':school:' + #reqVO.schoolId + ':college:' + #reqVO.collegeId + ':major:' + #reqVO.majorId"
                    + " + ':study:' + #reqVO.studyMode")
    public List<AppAdjustmentAdmitListItemRespVO> getAdmitList(AppAdjustmentAdmitListReqVO reqVO) {
        if (reqVO == null) {
            return Collections.emptyList();
        }
        LambdaQueryWrapperX<AdjustmentAdmitDO> wrapper = new LambdaQueryWrapperX<>();
        // 注意：LambdaQueryWrapperX 未重写 select 的返回类型，因此这里不要链式赋值
        wrapper.select(AdjustmentAdmitDO::getCandidateName,
                AdjustmentAdmitDO::getFirstSchoolName,
                AdjustmentAdmitDO::getFirstSchoolId,
                AdjustmentAdmitDO::getMajorCode,
                AdjustmentAdmitDO::getFirstScore,
                AdjustmentAdmitDO::getRetestScore,
                AdjustmentAdmitDO::getTotalScore);
        wrapper.eq(AdjustmentAdmitDO::getSchoolId, reqVO.getSchoolId())
                .eq(AdjustmentAdmitDO::getCollegeId, reqVO.getCollegeId())
                .eq(AdjustmentAdmitDO::getMajorId, reqVO.getMajorId())
                .eq(AdjustmentAdmitDO::getYear, reqVO.getYear())
                .eq(AdjustmentAdmitDO::getStudyMode, reqVO.getStudyMode());
        wrapper.orderByDesc(AdjustmentAdmitDO::getTotalScore)
                .orderByDesc(AdjustmentAdmitDO::getFirstScore)
                .orderByDesc(AdjustmentAdmitDO::getId);
        List<AdjustmentAdmitDO> list = adjustmentAdmitMapper.selectList(wrapper);
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }

        Integer year = reqVO.getYear() != null ? reqVO.getYear().intValue() : null;
        List<NationalScoreDO> nationalScores = year != null
                ? nationalLineEligibilityService.getNationalScoresWithFallback(year) : Collections.emptyList();
        Map<Long, String> firstSchoolAreaMap = resolveFirstSchoolAreaMap(list);

        List<AppAdjustmentAdmitListItemRespVO> resp = new ArrayList<>(list.size());
        for (AdjustmentAdmitDO item : list) {
            AppAdjustmentAdmitListItemRespVO vo = new AppAdjustmentAdmitListItemRespVO();
            vo.setCandidateName(maskCandidateName(item.getCandidateName()));
            vo.setFirstSchoolName(item.getFirstSchoolName());
            vo.setFirstScore(item.getFirstScore());
            vo.setRetestScore(item.getRetestScore());
            vo.setTotalScore(item.getTotalScore());
            fillDiffToNationalLine(vo, item, nationalScores, firstSchoolAreaMap);
            resp.add(vo);
        }
        return resp;
    }

    private Map<Long, String> resolveFirstSchoolAreaMap(List<AdjustmentAdmitDO> list) {
        if (CollUtil.isEmpty(list)) {
            return Collections.emptyMap();
        }
        List<Long> ids = list.stream()
                .map(AdjustmentAdmitDO::getFirstSchoolId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
        if (ids.isEmpty()) {
            return Collections.emptyMap();
        }
        List<SchoolDO> schools = schoolMapper.selectBatchIds(ids);
        if (CollUtil.isEmpty(schools)) {
            return Collections.emptyMap();
        }
        Map<Long, String> map = new HashMap<>(schools.size());
        for (SchoolDO s : schools) {
            if (s == null || s.getId() == null) {
                continue;
            }
            map.put(s.getId(), s.getProvinceArea());
        }
        return map;
    }

    private void fillDiffToNationalLine(AppAdjustmentAdmitListItemRespVO vo,
                                       AdjustmentAdmitDO item,
                                       List<NationalScoreDO> nationalScores,
                                       Map<Long, String> firstSchoolAreaMap) {
        if (vo == null || item == null || item.getFirstScore() == null) {
            return;
        }
        if (CollUtil.isEmpty(nationalScores)) {
            return;
        }
        String majorCode = StrUtil.trimToNull(item.getMajorCode());
        if (majorCode == null) {
            return;
        }
        String area = null;
        if (item.getFirstSchoolId() != null && firstSchoolAreaMap != null) {
            area = firstSchoolAreaMap.get(item.getFirstSchoolId());
        }
        area = StrUtil.trimToNull(area);
        if (area == null) {
            area = "A";
        }
        NationalScoreDO line = nationalLineEligibilityService.findMatchedNationalLine(nationalScores, area, majorCode);
        if (line == null || line.getTotal() == null) {
            return;
        }
        BigDecimal diff = item.getFirstScore().subtract(BigDecimal.valueOf(line.getTotal().intValue()));
        vo.setDiffToNationalLine(diff);
        vo.setAbnormal(diff.signum() < 0);
    }

    private String maskCandidateName(String name) {
        if (name == null) {
            return null;
        }
        String trimmed = name.trim();
        if (trimmed.isEmpty()) {
            return trimmed;
        }
        int[] cps = trimmed.codePoints().toArray();
        int n = cps.length;
        if (n <= 0) {
            return trimmed;
        }
        if (n == 1) {
            return "*";
        }
        String first = new String(cps, 0, 1);
        String last = new String(cps, n - 1, 1);
        if (n == 2) {
            return first + "*";
        }
        if (n == 3) {
            return first + "*" + last;
        }
        if (n == 4) {
            return first + "**" + last;
        }
        return first + "*".repeat(n - 2) + last;
    }
}

