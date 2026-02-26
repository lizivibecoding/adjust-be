package com.hongguoyan.module.biz.service.useradjustment;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.hongguoyan.framework.common.exception.ErrorCode;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.util.object.BeanUtils;
import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.hongguoyan.module.biz.controller.admin.useradjustment.vo.UserAdjustmentAdminAuditReqVO;
import com.hongguoyan.module.biz.controller.admin.useradjustment.vo.UserAdjustmentAdminCreateReqVO;
import com.hongguoyan.module.biz.controller.admin.useradjustment.vo.UserAdjustmentAdminPageReqVO;
import com.hongguoyan.module.biz.controller.admin.useradjustment.vo.UserAdjustmentAdminPageRespVO;
import com.hongguoyan.module.biz.dal.dataobject.major.MajorDO;
import com.hongguoyan.module.biz.dal.dataobject.publisher.PublisherDO;
import com.hongguoyan.module.biz.dal.dataobject.school.SchoolDO;
import com.hongguoyan.module.biz.dal.dataobject.schoolcollege.SchoolCollegeDO;
import com.hongguoyan.module.biz.dal.dataobject.schooldirection.SchoolDirectionDO;
import com.hongguoyan.module.biz.dal.dataobject.useradjustment.UserAdjustmentDO;
import com.hongguoyan.module.biz.dal.mysql.major.MajorMapper;
import com.hongguoyan.module.biz.dal.mysql.publisher.PublisherMapper;
import com.hongguoyan.module.biz.dal.mysql.school.SchoolMapper;
import com.hongguoyan.module.biz.dal.mysql.schoolcollege.SchoolCollegeMapper;
import com.hongguoyan.module.biz.dal.mysql.schooldirection.SchoolDirectionMapper;
import com.hongguoyan.module.biz.dal.mysql.useradjustment.UserAdjustmentMapper;
import com.hongguoyan.module.biz.enums.useradjustment.UserAdjustmentAuditStatusEnum;
import com.hongguoyan.module.biz.enums.useradjustment.UserAdjustmentSourceTypeEnum;
import com.hongguoyan.module.biz.service.projectconfig.ProjectConfigService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;

import static com.hongguoyan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.hongguoyan.module.biz.enums.ErrorCodeConstants.*;

@Service
@Validated
public class UserAdjustmentAdminServiceImpl implements UserAdjustmentAdminService {

    @Resource
    private UserAdjustmentMapper userAdjustmentMapper;
    @Resource
    private PublisherMapper publisherMapper;
    @Resource
    private SchoolDirectionMapper schoolDirectionMapper;
    @Resource
    private SchoolMapper schoolMapper;
    @Resource
    private SchoolCollegeMapper schoolCollegeMapper;
    @Resource
    private MajorMapper majorMapper;
    @Resource
    private ProjectConfigService projectConfigService;

    @Override
    public PageResult<UserAdjustmentAdminPageRespVO> getApprovedPage(UserAdjustmentAdminPageReqVO reqVO) {
        PageResult<UserAdjustmentDO> pageResult = userAdjustmentMapper.selectPage(reqVO, buildQuery(reqVO)
                .eq(UserAdjustmentDO::getStatus, 1)
                .eq(UserAdjustmentDO::getAuditStatus, UserAdjustmentAuditStatusEnum.APPROVED.getCode())
                .orderByDesc(UserAdjustmentDO::getId));
        return BeanUtils.toBean(pageResult, UserAdjustmentAdminPageRespVO.class);
    }

    @Override
    public PageResult<UserAdjustmentAdminPageRespVO> getAuditPage(UserAdjustmentAdminPageReqVO reqVO) {
        PageResult<UserAdjustmentDO> pageResult = userAdjustmentMapper.selectPage(reqVO, buildQuery(reqVO)
                .eq(UserAdjustmentDO::getAuditStatus, UserAdjustmentAuditStatusEnum.PENDING.getCode())
                .orderByDesc(UserAdjustmentDO::getId));
        return BeanUtils.toBean(pageResult, UserAdjustmentAdminPageRespVO.class);
    }

    private LambdaQueryWrapperX<UserAdjustmentDO> buildQuery(UserAdjustmentAdminPageReqVO reqVO) {
        LambdaQueryWrapperX<UserAdjustmentDO> query = new LambdaQueryWrapperX<UserAdjustmentDO>()
                .eqIfPresent(UserAdjustmentDO::getYear, reqVO != null ? reqVO.getYear() : null)
                .eqIfPresent(UserAdjustmentDO::getSourceType, reqVO != null ? reqVO.getSourceType() : null);
        String keyword = reqVO != null ? reqVO.getKeyword() : null;
        if (StrUtil.isNotBlank(keyword)) {
            String kw = keyword.trim();
            query.and(w -> w.like(UserAdjustmentDO::getSchoolName, kw)
                    .or().like(UserAdjustmentDO::getCollegeName, kw)
                    .or().like(UserAdjustmentDO::getMajorName, kw)
                    .or().like(UserAdjustmentDO::getMajorCode, kw)
                    .or().like(UserAdjustmentDO::getDirectionName, kw)
                    .or().like(UserAdjustmentDO::getTitle, kw));
        }
        return query;
    }

    @Override
    public Long createByAdmin(Long adminUserId, UserAdjustmentAdminCreateReqVO reqVO) {
        if (reqVO == null || reqVO.getDirectionId() == null) {
            throw exception(new ErrorCode(400, "directionId is required"));
        }
        Integer year = projectConfigService.getActiveYear();
        if (year == null) {
            throw exception(new ErrorCode(400, "activeYear is required"));
        }

        Integer sourceType = reqVO.getSourceType();
        if (sourceType == null) {
            throw exception(new ErrorCode(400, "sourceType is required"));
        }
        Long finalUserId;
        if (UserAdjustmentSourceTypeEnum.RUMOR.getCode().equals(sourceType)) {
            finalUserId = adminUserId;
        } else if (UserAdjustmentSourceTypeEnum.TEACHER.getCode().equals(sourceType)
                || UserAdjustmentSourceTypeEnum.SENIOR.getCode().equals(sourceType)) {
            if (reqVO.getPublisherUserId() == null) {
                throw exception(new ErrorCode(400, "publisherUserId is required"));
            }
            validatePublisherIdentityApproved(reqVO.getPublisherUserId(), sourceType);
            finalUserId = reqVO.getPublisherUserId();
        } else {
            throw exception(new ErrorCode(400, "sourceType invalid"));
        }

        UserAdjustmentDO toCreate = buildToSave(finalUserId, null, reqVO.getDirectionId(), year,
                reqVO.getAdjustCount(), reqVO.getAdjustLeft(), reqVO.getContact(), reqVO.getTitle(), reqVO.getRemark());
        toCreate.setId(null);
        toCreate.setSourceType(sourceType);
        toCreate.setAuditStatus(UserAdjustmentAuditStatusEnum.PENDING.getCode());
        toCreate.setStatus(0);
        toCreate.setPublishTime(LocalDateTime.now());
        toCreate.setViewCount(0);
        userAdjustmentMapper.insert(toCreate);
        return toCreate.getId();
    }

    @Override
    public void approve(Long adminUserId, UserAdjustmentAdminAuditReqVO reqVO) {
        UserAdjustmentDO existing = validateExists(reqVO != null ? reqVO.getId() : null);
        userAdjustmentMapper.update(null, new LambdaUpdateWrapper<UserAdjustmentDO>()
                .eq(UserAdjustmentDO::getId, existing.getId())
                .set(UserAdjustmentDO::getAuditStatus, UserAdjustmentAuditStatusEnum.APPROVED.getCode())
                .set(UserAdjustmentDO::getAuditUserId, adminUserId)
                .set(UserAdjustmentDO::getAuditTime, LocalDateTime.now())
                .set(UserAdjustmentDO::getAuditReason, null)
                .set(UserAdjustmentDO::getStatus, 1));
    }

    @Override
    public void reject(Long adminUserId, UserAdjustmentAdminAuditReqVO reqVO) {
        UserAdjustmentDO existing = validateExists(reqVO != null ? reqVO.getId() : null);
        String reason = reqVO != null ? reqVO.getReason() : null;
        if (StrUtil.isBlank(reason)) {
            throw exception(new ErrorCode(400, "reason is required"));
        }
        userAdjustmentMapper.update(null, new LambdaUpdateWrapper<UserAdjustmentDO>()
                .eq(UserAdjustmentDO::getId, existing.getId())
                .set(UserAdjustmentDO::getAuditStatus, UserAdjustmentAuditStatusEnum.REJECTED.getCode())
                .set(UserAdjustmentDO::getAuditUserId, adminUserId)
                .set(UserAdjustmentDO::getAuditTime, LocalDateTime.now())
                .set(UserAdjustmentDO::getAuditReason, reason.trim())
                .set(UserAdjustmentDO::getStatus, 0));
    }

    private UserAdjustmentDO validateExists(Long id) {
        if (id == null) {
            throw exception(new ErrorCode(400, "id is required"));
        }
        UserAdjustmentDO userAdjustment = userAdjustmentMapper.selectById(id);
        if (userAdjustment == null) {
            throw exception(USER_ADJUSTMENT_NOT_EXISTS);
        }
        return userAdjustment;
    }

    private void validatePublisherIdentityApproved(Long userId, Integer sourceType) {
        PublisherDO publisher = publisherMapper.selectOne(new LambdaQueryWrapperX<PublisherDO>()
                .eq(PublisherDO::getUserId, userId));
        if (publisher == null || publisher.getStatus() == null || publisher.getStatus() != 1) {
            throw exception(PUBLISHER_NOT_APPROVED);
        }
        Integer identityType = publisher.getIdentityType();
        if (identityType == null) {
            throw exception(PUBLISHER_IDENTITY_TYPE_INVALID);
        }
        if (UserAdjustmentSourceTypeEnum.TEACHER.getCode().equals(sourceType) && !identityType.equals(1)) {
            throw exception(PUBLISHER_IDENTITY_TYPE_INVALID);
        }
        if (UserAdjustmentSourceTypeEnum.SENIOR.getCode().equals(sourceType) && !identityType.equals(2)) {
            throw exception(PUBLISHER_IDENTITY_TYPE_INVALID);
        }
    }

    private UserAdjustmentDO buildToSave(Long userId,
                                        Long id,
                                        Long directionId,
                                        Integer year,
                                        Integer adjustCount,
                                        Integer adjustLeft,
                                        String contact,
                                        String title,
                                        String remark) {
        SchoolDirectionDO direction = schoolDirectionMapper.selectById(directionId);
        if (direction == null) {
            throw exception(SCHOOL_DIRECTION_NOT_EXISTS);
        }

        UserAdjustmentDO toSave = new UserAdjustmentDO();
        toSave.setId(id);
        toSave.setUserId(userId);
        toSave.setYear(year);
        toSave.setDirectionId(directionId);
        toSave.setDirectionCode(direction.getDirectionCode());
        toSave.setDirectionName(direction.getDirectionName());
        toSave.setStudyMode(direction.getStudyMode());

        toSave.setSchoolId(direction.getSchoolId());
        SchoolDO school = direction.getSchoolId() != null ? schoolMapper.selectById(direction.getSchoolId()) : null;
        toSave.setSchoolName(school != null ? StrUtil.blankToDefault(school.getSchoolName(), "") : "");

        toSave.setCollegeId(direction.getCollegeId());
        SchoolCollegeDO college = direction.getCollegeId() != null ? schoolCollegeMapper.selectById(direction.getCollegeId()) : null;
        toSave.setCollegeName(college != null ? StrUtil.blankToDefault(college.getName(), "") : "");

        toSave.setMajorId(direction.getMajorId());
        MajorDO major = direction.getMajorId() != null ? majorMapper.selectById(direction.getMajorId()) : null;
        if (major != null) {
            toSave.setMajorCode(StrUtil.blankToDefault(major.getCode(), ""));
            toSave.setMajorName(StrUtil.blankToDefault(major.getName(), ""));
            toSave.setDegreeType(major.getDegreeType() != null ? major.getDegreeType() : 0);
        } else {
            toSave.setMajorCode("");
            toSave.setMajorName("");
            toSave.setDegreeType(0);
        }

        toSave.setAdjustCount(adjustCount != null ? adjustCount : 0);
        toSave.setAdjustLeft(adjustLeft != null ? adjustLeft : 0);
        toSave.setContact(StrUtil.blankToDefault(contact, ""));
        toSave.setTitle(StrUtil.blankToDefault(title, ""));
        toSave.setRemark(StrUtil.blankToDefault(remark, ""));
        return toSave;
    }
}

