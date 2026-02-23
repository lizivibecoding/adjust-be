package com.hongguoyan.module.biz.cache.adjustmentadmit;

import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.hongguoyan.module.biz.cache.CacheNames;
import com.hongguoyan.module.biz.controller.app.adjustmentadmit.vo.AppAdjustmentAdmitListItemRespVO;
import com.hongguoyan.module.biz.controller.app.adjustmentadmit.vo.AppAdjustmentAdmitListReqVO;
import com.hongguoyan.module.biz.dal.dataobject.adjustmentadmit.AdjustmentAdmitDO;
import com.hongguoyan.module.biz.dal.mysql.adjustmentadmit.AdjustmentAdmitMapper;
import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * 录取名单-缓存。
 */
@Service
@Validated
public class AdjustmentAdmitCache {

    @Resource
    private AdjustmentAdmitMapper adjustmentAdmitMapper;

    @Cacheable(cacheNames = CacheNames.ADJUSTMENT_ADMIT_LIST,
            key = "'year:' + #reqVO.year"
                    + " + ':school:' + #reqVO.schoolId + ':college:' + #reqVO.collegeId + ':major:' + #reqVO.majorId"
                    + " + ':study:' + #reqVO.studyMode",
            sync = true)
    public List<AppAdjustmentAdmitListItemRespVO> getAdmitList(AppAdjustmentAdmitListReqVO reqVO) {
        if (reqVO == null) {
            return Collections.emptyList();
        }
        LambdaQueryWrapperX<AdjustmentAdmitDO> wrapper = new LambdaQueryWrapperX<>();
        // 注意：LambdaQueryWrapperX 未重写 select 的返回类型，因此这里不要链式赋值
        wrapper.select(AdjustmentAdmitDO::getCandidateName,
                AdjustmentAdmitDO::getFirstSchoolName,
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
        List<AppAdjustmentAdmitListItemRespVO> resp = new ArrayList<>(list.size());
        for (AdjustmentAdmitDO item : list) {
            AppAdjustmentAdmitListItemRespVO vo = new AppAdjustmentAdmitListItemRespVO();
            vo.setCandidateName(maskCandidateName(item.getCandidateName()));
            vo.setFirstSchoolName(item.getFirstSchoolName());
            vo.setFirstScore(item.getFirstScore());
            vo.setRetestScore(item.getRetestScore());
            vo.setTotalScore(item.getTotalScore());
            resp.add(vo);
        }
        return resp;
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

