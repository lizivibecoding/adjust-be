package com.hongguoyan.module.biz.service.useradjustmentapply;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import com.hongguoyan.module.biz.controller.app.useradjustmentapply.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.useradjustmentapply.UserAdjustmentApplyDO;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.util.object.BeanUtils;

import com.hongguoyan.module.biz.dal.mysql.useradjustmentapply.UserAdjustmentApplyMapper;
import com.hongguoyan.module.biz.dal.dataobject.useradjustment.UserAdjustmentDO;
import com.hongguoyan.module.biz.dal.mysql.useradjustment.UserAdjustmentMapper;
import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;

import static com.hongguoyan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.hongguoyan.framework.common.util.collection.CollectionUtils.convertList;
import static com.hongguoyan.framework.common.util.collection.CollectionUtils.diffList;
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

    // ====== Generated CRUD (for admin reuse) ======

    @Override
    public Long createUserAdjustmentApply(AppUserAdjustmentApplySaveReqVO createReqVO) {
        UserAdjustmentApplyDO userAdjustmentApply = BeanUtils.toBean(createReqVO, UserAdjustmentApplyDO.class);
        userAdjustmentApplyMapper.insert(userAdjustmentApply);
        return userAdjustmentApply.getId();
    }

    @Override
    public void updateUserAdjustmentApply(AppUserAdjustmentApplySaveReqVO updateReqVO) {
        validateUserAdjustmentApplyExists(updateReqVO.getId());
        UserAdjustmentApplyDO updateObj = BeanUtils.toBean(updateReqVO, UserAdjustmentApplyDO.class);
        userAdjustmentApplyMapper.updateById(updateObj);
    }

    @Override
    public void deleteUserAdjustmentApply(Long id) {
        validateUserAdjustmentApplyExists(id);
        userAdjustmentApplyMapper.deleteById(id);
    }

    @Override
    public void deleteUserAdjustmentApplyListByIds(List<Long> ids) {
        userAdjustmentApplyMapper.deleteByIds(ids);
    }

    @Override
    public UserAdjustmentApplyDO getUserAdjustmentApply(Long id) {
        return userAdjustmentApplyMapper.selectById(id);
    }

    @Override
    public PageResult<UserAdjustmentApplyDO> getUserAdjustmentApplyPage(AppUserAdjustmentApplyPageReqVO pageReqVO) {
        return userAdjustmentApplyMapper.selectPage(pageReqVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createUserAdjustmentApply(Long userId, AppUserAdjustmentApplySaveReqVO createReqVO) {
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
        UserAdjustmentApplyDO userAdjustmentApply = BeanUtils.toBean(createReqVO, UserAdjustmentApplyDO.class);
        userAdjustmentApply.setId(null);
        userAdjustmentApply.setUserId(userId);
        userAdjustmentApplyMapper.insert(userAdjustmentApply);
        return userAdjustmentApply.getId();
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
            postIds.add(apply.getUserAdjustmentId());
        }
        Map<Long, UserAdjustmentDO> postMap = new HashMap<>();
        List<UserAdjustmentDO> posts = userAdjustmentMapper.selectBatchIds(postIds);
        if (posts != null) {
            for (UserAdjustmentDO post : posts) {
                postMap.put(post.getId(), post);
            }
        }
        List<AppUserAdjustmentApplyMyItemRespVO> list = new ArrayList<>(applies.size());
        for (UserAdjustmentApplyDO apply : applies) {
            UserAdjustmentDO post = postMap.get(apply.getUserAdjustmentId());
            AppUserAdjustmentApplyMyItemRespVO vo = new AppUserAdjustmentApplyMyItemRespVO();
            vo.setUserAdjustmentId(apply.getUserAdjustmentId());
            vo.setApplyTime(apply.getCreateTime());
            if (post != null) {
                vo.setTitle(post.getTitle());
                vo.setMajorText(buildMajorText(post));
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

    private String buildMajorText(UserAdjustmentDO post) {
        StringBuilder sb = new StringBuilder();
        if (StrUtil.isNotBlank(post.getMajorCode())) {
            sb.append("（").append(post.getMajorCode()).append("）");
        }
        sb.append(StrUtil.blankToDefault(post.getMajorName(), ""));
        if (StrUtil.isNotBlank(post.getDirectionName())) {
            sb.append("-").append(post.getDirectionName());
        }
        if (post.getStudyMode() != null) {
            sb.append("-").append(post.getStudyMode() == 1 ? "全日制" : "非全日制");
        }
        return sb.toString();
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

    private void validateUserAdjustmentApplyExists(Long id) {
        if (userAdjustmentApplyMapper.selectById(id) == null) {
            throw exception(USER_ADJUSTMENT_APPLY_NOT_EXISTS);
        }
    }

}