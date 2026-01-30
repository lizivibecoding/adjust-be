package com.hongguoyan.module.biz.service.useradjustment;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import com.hongguoyan.module.biz.controller.app.useradjustment.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.major.MajorDO;
import com.hongguoyan.module.biz.dal.dataobject.school.SchoolDO;
import com.hongguoyan.module.biz.dal.dataobject.schoolcollege.SchoolCollegeDO;
import com.hongguoyan.module.biz.dal.dataobject.schooldirection.SchoolDirectionDO;
import com.hongguoyan.module.biz.dal.dataobject.useradjustment.UserAdjustmentDO;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.util.object.BeanUtils;

import com.hongguoyan.module.biz.dal.mysql.major.MajorMapper;
import com.hongguoyan.module.biz.dal.mysql.school.SchoolMapper;
import com.hongguoyan.module.biz.dal.mysql.schoolcollege.SchoolCollegeMapper;
import com.hongguoyan.module.biz.dal.mysql.schooldirection.SchoolDirectionMapper;
import com.hongguoyan.module.biz.dal.mysql.useradjustment.UserAdjustmentMapper;
import com.hongguoyan.module.biz.dal.dataobject.publisher.PublisherDO;
import com.hongguoyan.module.biz.dal.mysql.publisher.PublisherMapper;
import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.hongguoyan.module.biz.dal.mysql.useradjustmentapply.UserAdjustmentApplyMapper;

import static com.hongguoyan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.hongguoyan.framework.common.util.collection.CollectionUtils.convertList;
import static com.hongguoyan.framework.common.util.collection.CollectionUtils.diffList;
import static com.hongguoyan.module.biz.enums.ErrorCodeConstants.*;

/**
 * 用户发布调剂 Service 实现类
 *
 * @author hgy
 */
@Service
@Validated
public class UserAdjustmentServiceImpl implements UserAdjustmentService {

    @Resource
    private UserAdjustmentMapper userAdjustmentMapper;
    @Resource
    private SchoolMapper schoolMapper;
    @Resource
    private MajorMapper majorMapper;
    @Resource
    private SchoolDirectionMapper schoolDirectionMapper;
    @Resource
    private SchoolCollegeMapper schoolCollegeMapper;
    @Resource
    private PublisherMapper publisherMapper;
    @Resource
    private UserAdjustmentApplyMapper userAdjustmentApplyMapper;

    @Override
    public Long createUserAdjustment(Long userId, AppUserAdjustmentCreateReqVO createReqVO) {
        validatePublisherApproved(userId);
        UserAdjustmentDO toCreate = buildToSave(userId, null, createReqVO.getDirectionId(),
                createReqVO.getYear(), createReqVO.getAdjustCount(), createReqVO.getAdjustLeft(),
                createReqVO.getContact(), createReqVO.getTitle(), createReqVO.getRemark());
        toCreate.setId(null);
        toCreate.setStatus(1);
        toCreate.setPublishTime(LocalDateTime.now());
        toCreate.setViewCount(0);
        userAdjustmentMapper.insert(toCreate);
        return toCreate.getId();
    }

    @Override
    public void updateUserAdjustment(Long userId, AppUserAdjustmentUpdateReqVO updateReqVO) {
        validatePublisherApproved(userId);
        UserAdjustmentDO existing = validateUserAdjustmentExists(updateReqVO.getId());
        if (!userId.equals(existing.getUserId())) {
            throw exception(USER_ADJUSTMENT_NOT_EXISTS);
        }
        UserAdjustmentDO toUpdate = buildToSave(userId, existing.getId(), updateReqVO.getDirectionId(),
                updateReqVO.getYear(), updateReqVO.getAdjustCount(), updateReqVO.getAdjustLeft(),
                updateReqVO.getContact(), updateReqVO.getTitle(), updateReqVO.getRemark());
        // keep immutable fields
        toUpdate.setUserId(existing.getUserId());
        toUpdate.setPublishTime(existing.getPublishTime());
        toUpdate.setViewCount(existing.getViewCount());
        toUpdate.setStatus(existing.getStatus());
        userAdjustmentMapper.updateById(toUpdate);
    }

    @Override
    public PageResult<AppUserAdjustmentListRespVO> getUserAdjustmentPublicPage(AppUserAdjustmentPageReqVO pageReqVO) {
        // only show open records by default
        if (pageReqVO.getStatus() == null) {
            pageReqVO.setStatus(1);
        }
        PageResult<UserAdjustmentDO> pageResult = userAdjustmentMapper.selectPage(pageReqVO);
        List<UserAdjustmentDO> records = pageResult.getList();
        Map<Long, String> schoolLogoMap = buildSchoolLogoMap(records);
        Map<Long, String> majorLevel1NameMap = buildMajorLevel1NameMap(records);
        List<AppUserAdjustmentListRespVO> list = new ArrayList<>();
        for (UserAdjustmentDO item : records) {
            list.add(toListResp(item, schoolLogoMap, majorLevel1NameMap));
        }
        return new PageResult<>(list, pageResult.getTotal());
    }

    @Override
    public PageResult<AppUserAdjustmentListRespVO> getMyUserAdjustmentPage(Long userId, AppUserAdjustmentPageReqVO pageReqVO) {
        pageReqVO.setUserId(userId);
        PageResult<UserAdjustmentDO> pageResult = userAdjustmentMapper.selectPage(pageReqVO);
        List<UserAdjustmentDO> records = pageResult.getList();
        Map<Long, String> schoolLogoMap = buildSchoolLogoMap(records);
        Map<Long, String> majorLevel1NameMap = buildMajorLevel1NameMap(records);
        List<AppUserAdjustmentListRespVO> list = new ArrayList<>();
        for (UserAdjustmentDO item : records) {
            list.add(toListResp(item, schoolLogoMap, majorLevel1NameMap));
        }
        return new PageResult<>(list, pageResult.getTotal());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AppUserAdjustmentDetailRespVO getUserAdjustmentDetail(Long id, Long viewerUserId) {
        UserAdjustmentDO userAdjustment = validateUserAdjustmentExists(id);
        // view count +1 (best-effort)
        Integer current = userAdjustment.getViewCount() != null ? userAdjustment.getViewCount() : 0;
        userAdjustmentMapper.update(null, new com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper<UserAdjustmentDO>()
                .eq(UserAdjustmentDO::getId, id)
                .set(UserAdjustmentDO::getViewCount, current + 1));

        boolean canViewContact = viewerUserId != null && (viewerUserId.equals(userAdjustment.getUserId())
                || userAdjustmentApplyMapper.selectCount(new LambdaQueryWrapperX<com.hongguoyan.module.biz.dal.dataobject.useradjustmentapply.UserAdjustmentApplyDO>()
                        .eq(com.hongguoyan.module.biz.dal.dataobject.useradjustmentapply.UserAdjustmentApplyDO::getUserAdjustmentId, id)
                        .eq(com.hongguoyan.module.biz.dal.dataobject.useradjustmentapply.UserAdjustmentApplyDO::getUserId, viewerUserId)) > 0);

        AppUserAdjustmentDetailRespVO respVO = BeanUtils.toBean(userAdjustment, AppUserAdjustmentDetailRespVO.class);
        respVO.setViewCount(current + 1);
        if (!canViewContact) {
            respVO.setContact("******（申请调剂后可查看）");
        }
        return respVO;
    }

    private UserAdjustmentDO validateUserAdjustmentExists(Long id) {
        UserAdjustmentDO userAdjustment = userAdjustmentMapper.selectById(id);
        if (userAdjustment == null) {
            throw exception(USER_ADJUSTMENT_NOT_EXISTS);
        }
        return userAdjustment;
    }

    private void validatePublisherApproved(Long userId) {
        PublisherDO publisher = publisherMapper.selectOne(new LambdaQueryWrapperX<PublisherDO>()
                .eq(PublisherDO::getUserId, userId));
        if (publisher == null || publisher.getStatus() == null || publisher.getStatus() != 1) {
            throw exception(PUBLISHER_NOT_APPROVED);
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
        toSave.setStudyMode(mapStudyMode(direction.getStudyMode()));

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

    private int mapStudyMode(String studyMode) {
        if (StrUtil.isBlank(studyMode)) {
            return 0;
        }
        String s = studyMode.trim();
        if ("全日制".equals(s)) {
            return 1;
        }
        if ("非全日制".equals(s)) {
            return 2;
        }
        return 0;
    }

    private AppUserAdjustmentListRespVO toListResp(UserAdjustmentDO item,
                                                   Map<Long, String> schoolLogoMap,
                                                   Map<Long, String> majorLevel1NameMap) {
        AppUserAdjustmentListRespVO vo = new AppUserAdjustmentListRespVO();
        vo.setId(item.getId());
        vo.setTitle(item.getTitle());
        vo.setSchoolName(item.getSchoolName());
        vo.setSchoolLogo(schoolLogoMap.get(item.getSchoolId()));
        vo.setMajorLevel1Name(majorLevel1NameMap.get(item.getMajorId()));
        vo.setMajorCode(item.getMajorCode());
        vo.setMajorName(item.getMajorName());
        vo.setDegreeType(item.getDegreeType());
        vo.setAdjustCount(item.getAdjustCount());
        vo.setPublishTime(item.getPublishTime());
        return vo;
    }

    private Map<Long, String> buildSchoolLogoMap(List<UserAdjustmentDO> records) {
        if (records == null || records.isEmpty()) {
            return Collections.emptyMap();
        }
        Set<Long> schoolIds = new HashSet<>();
        for (UserAdjustmentDO item : records) {
            if (item != null && item.getSchoolId() != null) {
                schoolIds.add(item.getSchoolId());
            }
        }
        if (schoolIds.isEmpty()) {
            return Collections.emptyMap();
        }
        List<SchoolDO> schools = schoolMapper.selectBatchIds(schoolIds);
        if (schools == null || schools.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<Long, String> map = new HashMap<>();
        for (SchoolDO school : schools) {
            if (school != null && school.getId() != null) {
                map.put(school.getId(), school.getSchoolLogo());
            }
        }
        return map;
    }

    private Map<Long, String> buildMajorLevel1NameMap(List<UserAdjustmentDO> records) {
        if (records == null || records.isEmpty()) {
            return Collections.emptyMap();
        }
        Set<Long> majorIds = new HashSet<>();
        for (UserAdjustmentDO item : records) {
            if (item != null && item.getMajorId() != null) {
                majorIds.add(item.getMajorId());
            }
        }
        if (majorIds.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<Long, MajorDO> majorMap = new HashMap<>();
        Set<Long> toLoad = new HashSet<>(majorIds);
        // best-effort: load ancestor chain by parentId
        for (int i = 0; i < 10 && !toLoad.isEmpty(); i++) {
            List<MajorDO> majors = majorMapper.selectBatchIds(toLoad);
            toLoad.clear();
            if (majors == null || majors.isEmpty()) {
                break;
            }
            for (MajorDO major : majors) {
                if (major == null || major.getId() == null) {
                    continue;
                }
                majorMap.put(major.getId(), major);
                Long parentId = major.getParentId();
                if (parentId != null && !majorMap.containsKey(parentId)) {
                    toLoad.add(parentId);
                }
            }
        }
        Map<Long, String> result = new HashMap<>();
        for (Long majorId : majorIds) {
            MajorDO cur = majorMap.get(majorId);
            while (cur != null && cur.getLevel() != null && cur.getLevel() != 1) {
                Long parentId = cur.getParentId();
                if (parentId == null) {
                    break;
                }
                cur = majorMap.get(parentId);
            }
            result.put(majorId, cur != null && cur.getLevel() != null && cur.getLevel() == 1 ? cur.getName() : "");
        }
        return result;
    }

}