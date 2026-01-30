package com.hongguoyan.module.biz.service.useradjustmentapply;

import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import com.hongguoyan.module.biz.controller.app.useradjustmentapply.vo.*;
import com.hongguoyan.module.biz.controller.app.useradjustment.vo.AppUserAdjustmentListRespVO;
import com.hongguoyan.module.biz.dal.dataobject.major.MajorDO;
import com.hongguoyan.module.biz.dal.dataobject.school.SchoolDO;
import com.hongguoyan.module.biz.dal.dataobject.useradjustmentapply.UserAdjustmentApplyDO;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.util.object.BeanUtils;

import com.hongguoyan.module.biz.dal.mysql.major.MajorMapper;
import com.hongguoyan.module.biz.dal.mysql.school.SchoolMapper;
import com.hongguoyan.module.biz.dal.mysql.useradjustmentapply.UserAdjustmentApplyMapper;
import com.hongguoyan.module.biz.dal.dataobject.useradjustment.UserAdjustmentDO;
import com.hongguoyan.module.biz.dal.mysql.useradjustment.UserAdjustmentMapper;
import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;

import static com.hongguoyan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.hongguoyan.module.biz.enums.ErrorCodeConstants.*;

/**
 * 用户发布调剂申请记录 Service 实现类
 *
 * @author hgy
 */
@Service
@Validated
public class UserAdjustmentApplyServiceImpl implements UserAdjustmentApplyService {

    @Resource
    private UserAdjustmentApplyMapper userAdjustmentApplyMapper;
    @Resource
    private UserAdjustmentMapper userAdjustmentMapper;
    @Resource
    private SchoolMapper schoolMapper;
    @Resource
    private MajorMapper majorMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createUserAdjustmentApply(Long userId, AppUserAdjustmentApplyCreateReqVO createReqVO) {
        UserAdjustmentDO userAdjustment = userAdjustmentMapper.selectById(createReqVO.getUserAdjustmentId());
        if (userAdjustment == null) {
            throw exception(USER_ADJUSTMENT_NOT_EXISTS);
        }
        if (userAdjustment.getStatus() != null && userAdjustment.getStatus() == 0) {
            throw exception(USER_ADJUSTMENT_CLOSED);
        }
        if (userId.equals(userAdjustment.getUserId())) {
            throw exception(USER_ADJUSTMENT_SELF_APPLY_NOT_ALLOWED);
        }
        // prevent duplicate apply
        Long exists = userAdjustmentApplyMapper.selectCount(new LambdaQueryWrapperX<UserAdjustmentApplyDO>()
                .eq(UserAdjustmentApplyDO::getUserAdjustmentId, createReqVO.getUserAdjustmentId())
                .eq(UserAdjustmentApplyDO::getUserId, userId));
        if (exists != null && exists > 0) {
            throw exception(USER_ADJUSTMENT_ALREADY_APPLIED);
        }
        SchoolDO firstSchool = schoolMapper.selectById(createReqVO.getFirstSchoolId());
        if (firstSchool == null) {
            throw exception(SCHOOL_NOT_EXISTS);
        }
        MajorDO firstMajor = majorMapper.selectById(createReqVO.getFirstMajorId());
        if (firstMajor == null || Boolean.TRUE.equals(firstMajor.getDeleted())) {
            throw exception(MAJOR_NOT_EXISTS);
        }
        BigDecimal totalScore = createReqVO.getTotalScore();
        if (totalScore == null) {
            totalScore = safe(createReqVO.getSubjectScore1())
                    .add(safe(createReqVO.getSubjectScore2()))
                    .add(safe(createReqVO.getSubjectScore3()))
                    .add(safe(createReqVO.getSubjectScore4()));
        }

        UserAdjustmentApplyDO userAdjustmentApply = new UserAdjustmentApplyDO();
        userAdjustmentApply.setId(null);
        userAdjustmentApply.setUserId(userId);
        userAdjustmentApply.setUserAdjustmentId(createReqVO.getUserAdjustmentId());
        userAdjustmentApply.setCandidateName(StrUtil.blankToDefault(createReqVO.getCandidateName(), ""));
        userAdjustmentApply.setContact(StrUtil.blankToDefault(createReqVO.getContact(), ""));

        userAdjustmentApply.setFirstSchoolId(createReqVO.getFirstSchoolId());
        userAdjustmentApply.setFirstSchoolName(StrUtil.blankToDefault(firstSchool.getSchoolName(), ""));

        userAdjustmentApply.setFirstMajorId(createReqVO.getFirstMajorId());
        userAdjustmentApply.setFirstMajorCode(StrUtil.blankToDefault(firstMajor.getCode(), ""));
        userAdjustmentApply.setFirstMajorName(StrUtil.blankToDefault(firstMajor.getName(), ""));

        userAdjustmentApply.setSubjectScore1(createReqVO.getSubjectScore1());
        userAdjustmentApply.setSubjectScore2(createReqVO.getSubjectScore2());
        userAdjustmentApply.setSubjectScore3(createReqVO.getSubjectScore3());
        userAdjustmentApply.setSubjectScore4(createReqVO.getSubjectScore4());
        userAdjustmentApply.setTotalScore(totalScore);
        userAdjustmentApply.setNote(StrUtil.blankToDefault(createReqVO.getNote(), ""));

        userAdjustmentApplyMapper.insert(userAdjustmentApply);
        return userAdjustmentApply.getId();
    }

    private BigDecimal safe(BigDecimal v) {
        return v != null ? v : BigDecimal.ZERO;
    }

    @Override
    public PageResult<AppUserAdjustmentApplyMyItemRespVO> getMyAppliedPage(Long userId, AppUserAdjustmentApplyPageReqVO pageReqVO) {
        pageReqVO.setUserId(userId);
        PageResult<UserAdjustmentApplyDO> pageResult = userAdjustmentApplyMapper.selectPage(pageReqVO);
        List<UserAdjustmentApplyDO> applies = pageResult.getList();
        if (applies == null || applies.isEmpty()) {
            return new PageResult<>(Collections.emptyList(), pageResult.getTotal());
        }
        // fetch related posts
        Set<Long> postIds = new HashSet<>();
        for (UserAdjustmentApplyDO apply : applies) {
            if (apply != null && apply.getUserAdjustmentId() != null) {
                postIds.add(apply.getUserAdjustmentId());
            }
        }
        List<UserAdjustmentDO> posts = postIds.isEmpty() ? Collections.emptyList() : userAdjustmentMapper.selectBatchIds(postIds);
        Map<Long, UserAdjustmentDO> postMap = new HashMap<>();
        if (posts != null) {
            for (UserAdjustmentDO post : posts) {
                if (post != null && post.getId() != null) {
                    postMap.put(post.getId(), post);
                }
            }
        }

        Map<Long, String> schoolLogoMap = buildSchoolLogoMap(posts);
        Map<Long, String> majorLevel1NameMap = buildMajorLevel1NameMap(posts);
        List<AppUserAdjustmentApplyMyItemRespVO> list = new ArrayList<>(applies.size());
        for (UserAdjustmentApplyDO apply : applies) {
            UserAdjustmentDO post = postMap.get(apply.getUserAdjustmentId());
            AppUserAdjustmentApplyMyItemRespVO vo = new AppUserAdjustmentApplyMyItemRespVO();
            vo.setApplyTime(apply.getCreateTime());
            if (post != null) {
                AppUserAdjustmentListRespVO base = toListResp(post, schoolLogoMap, majorLevel1NameMap);
                BeanUtils.copyProperties(base, vo);
            }
            list.add(vo);
        }
        return new PageResult<>(list, pageResult.getTotal());
    }

    @Override
    public List<AppUserAdjustmentApplicantListItemRespVO> getApplicantList(Long publisherUserId, Long userAdjustmentId) {
        UserAdjustmentDO post = userAdjustmentMapper.selectById(userAdjustmentId);
        if (post == null || !publisherUserId.equals(post.getUserId())) {
            throw exception(USER_ADJUSTMENT_NOT_EXISTS);
        }
        List<UserAdjustmentApplyDO> list = userAdjustmentApplyMapper.selectList(new LambdaQueryWrapperX<UserAdjustmentApplyDO>()
                .eq(UserAdjustmentApplyDO::getUserAdjustmentId, userAdjustmentId)
                .orderByDesc(UserAdjustmentApplyDO::getId));
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }
        List<AppUserAdjustmentApplicantListItemRespVO> resp = new ArrayList<>(list.size());
        for (UserAdjustmentApplyDO item : list) {
            AppUserAdjustmentApplicantListItemRespVO vo = new AppUserAdjustmentApplicantListItemRespVO();
            vo.setId(item.getId());
            vo.setCandidateName(item.getCandidateName());
            vo.setApplyTime(item.getCreateTime());
            resp.add(vo);
        }
        return resp;
    }

    @Override
    public AppUserAdjustmentApplicantDetailRespVO getApplicantDetail(Long publisherUserId, Long applyId) {
        UserAdjustmentApplyDO apply = userAdjustmentApplyMapper.selectById(applyId);
        if (apply == null) {
            throw exception(USER_ADJUSTMENT_APPLY_NOT_EXISTS);
        }
        UserAdjustmentDO post = userAdjustmentMapper.selectById(apply.getUserAdjustmentId());
        if (post == null || !publisherUserId.equals(post.getUserId())) {
            throw exception(USER_ADJUSTMENT_APPLY_NOT_EXISTS);
        }
        AppUserAdjustmentApplicantDetailRespVO vo = BeanUtils.toBean(apply, AppUserAdjustmentApplicantDetailRespVO.class);
        vo.setContact(maskContact(apply.getContact()));
        return vo;
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

    private String maskContact(String contact) {
        if (StrUtil.isBlank(contact)) {
            return "******";
        }
        String s = contact.trim();
        if (s.matches("^1\\d{10}$")) {
            return s.substring(0, 3) + "****" + s.substring(7);
        }
        if (s.length() <= 2) {
            return "*";
        }
        return s.substring(0, 2) + "****" + s.substring(Math.max(s.length() - 2, 0));
    }

}