package com.hongguoyan.module.biz.service.adjustment;

import cn.hutool.core.util.StrUtil;
import com.hongguoyan.framework.common.exception.ErrorCode;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.module.biz.controller.admin.adjustment.vo.AdjustmentAdmitPageReqVO;
import com.hongguoyan.module.biz.controller.admin.adjustment.vo.AdjustmentAdmitPageRespVO;
import com.hongguoyan.module.biz.controller.admin.adjustment.vo.AdjustmentCreateReqVO;
import com.hongguoyan.module.biz.controller.admin.adjustment.vo.AdjustmentPageReqVO;
import com.hongguoyan.module.biz.controller.admin.adjustment.vo.AdjustmentPageRespVO;
import com.hongguoyan.module.biz.cache.adjustment.AdjustmentDetailCache;
import com.hongguoyan.module.biz.cache.adjustment.AdjustmentUpdateStatsCache;
import com.hongguoyan.module.biz.dal.dataobject.adjustment.AdjustmentDO;
import com.hongguoyan.module.biz.dal.dataobject.major.MajorDO;
import com.hongguoyan.module.biz.dal.dataobject.school.SchoolDO;
import com.hongguoyan.module.biz.dal.dataobject.schoolcollege.SchoolCollegeDO;
import com.hongguoyan.module.biz.dal.dataobject.schooldirection.SchoolDirectionDO;
import com.hongguoyan.module.biz.dal.mysql.adjustment.AdjustmentMapper;
import com.hongguoyan.module.biz.dal.mysql.adjustmentadmit.AdjustmentAdmitMapper;
import com.hongguoyan.module.biz.dal.mysql.major.MajorMapper;
import com.hongguoyan.module.biz.dal.mysql.school.SchoolMapper;
import com.hongguoyan.module.biz.dal.mysql.schoolcollege.SchoolCollegeMapper;
import com.hongguoyan.module.biz.framework.config.AdjustProperties;
import com.hongguoyan.module.biz.service.schooldirection.SchoolDirectionService;
import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.hongguoyan.module.infra.api.file.FileApi;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.time.LocalDateTime;

import static com.hongguoyan.framework.common.exception.util.ServiceExceptionUtil.exception;

@Service
@Validated
public class AdjustmentAdminServiceImpl implements AdjustmentAdminService {

    @Resource
    private AdjustmentMapper adjustmentMapper;

    @Resource
    private AdjustmentAdmitMapper adjustmentAdmitMapper;
    @Resource
    private FileApi fileApi;
    @Resource
    private SchoolDirectionService schoolDirectionService;
    @Resource
    private SchoolMapper schoolMapper;
    @Resource
    private SchoolCollegeMapper schoolCollegeMapper;
    @Resource
    private MajorMapper majorMapper;
    @Resource
    private AdjustProperties adjustProperties;
    @Resource
    private AdjustmentDetailCache adjustmentDetailCache;
    @Resource
    private AdjustmentUpdateStatsCache adjustmentUpdateStatsCache;

    @Override
    public PageResult<AdjustmentPageRespVO> getAdjustmentPage(AdjustmentPageReqVO reqVO) {
        if (reqVO != null && StrUtil.isNotBlank(reqVO.getKeyword())) {
            reqVO.setKeyword(reqVO.getKeyword().trim());
        }
        if (reqVO != null && StrUtil.isNotBlank(reqVO.getMajorCode())) {
            reqVO.setMajorCode(reqVO.getMajorCode().trim());
        }
        if (reqVO != null && StrUtil.isNotBlank(reqVO.getProvinceCode())) {
            reqVO.setProvinceCode(reqVO.getProvinceCode().trim());
        }
        PageResult<AdjustmentPageRespVO> pageResult = adjustmentMapper.selectAdminAdjustmentPage(reqVO);
        if (pageResult == null || pageResult.getList() == null || pageResult.getList().isEmpty()) {
            return pageResult != null ? pageResult : new PageResult<>(Collections.emptyList(), 0L);
        }
        List<AdjustmentPageRespVO> list = new ArrayList<>(pageResult.getList().size());
        for (AdjustmentPageRespVO item : pageResult.getList()) {
            if (item == null) {
                continue;
            }
            item.setSchoolLogoUrl(fileApi.buildStaticUrl(item.getSchoolLogo()));
            // do not expose stored path in admin list response
            item.setSchoolLogo(null);
            list.add(item);
        }
        return new PageResult<>(list, pageResult.getTotal());
    }

    @Override
    public List<Integer> getYearList() {
        return adjustmentMapper.selectAdminYearList();
    }

    @Override
    public PageResult<AdjustmentAdmitPageRespVO> getAdmitPage(AdjustmentAdmitPageReqVO reqVO) {
        if (reqVO != null && StrUtil.isNotBlank(reqVO.getKeyword())) {
            reqVO.setKeyword(reqVO.getKeyword().trim());
        }
        PageResult<AdjustmentAdmitPageRespVO> pageResult = adjustmentAdmitMapper.selectAdminAdmitPage(reqVO);
        if (pageResult == null || pageResult.getList() == null || pageResult.getList().isEmpty()) {
            return pageResult != null ? pageResult : new PageResult<AdjustmentAdmitPageRespVO>(Collections.emptyList(), 0L);
        }
        List<AdjustmentAdmitPageRespVO> list = new ArrayList<>(pageResult.getList().size());
        for (AdjustmentAdmitPageRespVO item : pageResult.getList()) {
            if (item == null) {
                continue;
            }
            item.setCandidateName(maskCandidateName(item.getCandidateName()));
            list.add(item);
        }
        return new PageResult<>(list, pageResult.getTotal());
    }

    @Override
    public Long createAdjustment(AdjustmentCreateReqVO reqVO) {
        if (reqVO == null || reqVO.getDirectionId() == null) {
            throw exception(new ErrorCode(400, "directionId is required"));
        }
        Integer activeYear = adjustProperties.getActiveYear();

        Long directionId = reqVO.getDirectionId();
        SchoolDirectionDO direction = schoolDirectionService.getSchoolDirection(directionId);
        if (direction == null) {
            throw exception(new ErrorCode(400, "direction not exists"));
        }
        if (direction.getYear() == null || !direction.getYear().equals(activeYear)) {
            throw exception(new ErrorCode(400, "direction year invalid"));
        }

        Long exists = adjustmentMapper.selectCount(new LambdaQueryWrapperX<AdjustmentDO>()
                .eq(AdjustmentDO::getYear, activeYear)
                .eq(AdjustmentDO::getDirectionId, directionId));
        if (exists != null && exists > 0) {
            throw exception(new ErrorCode(400, "adjustment already exists"));
        }

        Long schoolId = direction.getSchoolId();
        Long collegeId = direction.getCollegeId();
        Long majorId = direction.getMajorId();
        Integer studyMode = direction.getStudyMode() != null ? direction.getStudyMode() : 1;

        SchoolDO school = schoolMapper.selectById(schoolId);
        if (school == null) {
            throw exception(new ErrorCode(400, "school not exists"));
        }
        if (collegeId == null) {
            throw exception(new ErrorCode(400, "collegeId is required"));
        }
        SchoolCollegeDO college = schoolCollegeMapper.selectById(collegeId);
        if (college == null) {
            throw exception(new ErrorCode(400, "college not exists"));
        }
        MajorDO major = majorMapper.selectById(majorId);
        if (major == null) {
            throw exception(new ErrorCode(400, "major not exists"));
        }

        String directionCode = StrUtil.blankToDefault(direction.getDirectionCode(), "00");
        String directionName = StrUtil.blankToDefault(direction.getDirectionName(), "不区分研究方向");

        AdjustmentDO adjustment = new AdjustmentDO();
        adjustment.setId(null);
        adjustment.setYear(activeYear);

        adjustment.setSourceType(3); // 人工/第三方
        adjustment.setSourceUrl(StrUtil.trimToNull(reqVO.getSourceUrl()));

        adjustment.setSchoolId(schoolId);
        adjustment.setSchoolName(StrUtil.blankToDefault(school.getSchoolName(), ""));
        adjustment.setCollegeId(collegeId);
        adjustment.setCollegeName(StrUtil.blankToDefault(college.getName(), ""));

        adjustment.setMajorId(majorId);
        adjustment.setMajorCode(StrUtil.blankToDefault(major.getCode(), ""));
        adjustment.setMajorName(StrUtil.blankToDefault(major.getName(), ""));
        adjustment.setDegreeType(major.getDegreeType() != null ? major.getDegreeType() : 0);

        adjustment.setDirectionId(directionId);
        adjustment.setDirectionCode(directionCode);
        adjustment.setDirectionName(directionName);
        adjustment.setStudyMode(studyMode);
        adjustment.setSubjects(direction.getSubjects());

        adjustment.setAdjustCount(reqVO.getAdjustCount());
        adjustment.setRemark(StrUtil.blankToDefault(reqVO.getRemark(), ""));

        adjustment.setStatus(1);
        adjustment.setViewCount(0);
        adjustment.setHotScore(0L);
        adjustment.setPublishTime(LocalDateTime.now());

        adjustmentMapper.insert(adjustment);

        // 定向清理缓存（App 详情按 year/school/college/major/studyMode）
        adjustmentDetailCache.evictDetailRows(schoolId, majorId, collegeId, activeYear, studyMode);
        // 调剂更新统计缓存
        adjustmentUpdateStatsCache.evictDefault();
        return adjustment.getId();
    }

    /**
     * Mask candidate name for display.
     * <p>
     * Rules:
     * - 2 chars: mask last char (e.g. 张三 -> 张*)
     * - 3 chars: mask middle char (e.g. 王小明 -> 王*明)
     * - 4 chars: mask middle two chars (e.g. 欧阳娜娜 -> 欧**娜)
     * - other lengths: keep first & last, mask the rest
     */
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

