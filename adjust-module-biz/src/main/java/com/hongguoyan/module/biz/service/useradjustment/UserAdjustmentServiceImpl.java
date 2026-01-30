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
import com.hongguoyan.module.biz.dal.dataobject.useradjustment.UserAdjustmentDO;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.util.object.BeanUtils;

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
    private PublisherMapper publisherMapper;
    @Resource
    private UserAdjustmentApplyMapper userAdjustmentApplyMapper;

    @Override
    public Long createUserAdjustment(Long userId, AppUserAdjustmentSaveReqVO createReqVO) {
        validatePublisherApproved(userId);
        UserAdjustmentDO userAdjustment = BeanUtils.toBean(createReqVO, UserAdjustmentDO.class);
        userAdjustment.setId(null);
        userAdjustment.setUserId(userId);
        if (userAdjustment.getStatus() == null) {
            userAdjustment.setStatus(1);
        }
        if (userAdjustment.getPublishTime() == null) {
            userAdjustment.setPublishTime(LocalDateTime.now());
        }
        if (userAdjustment.getViewCount() == null) {
            userAdjustment.setViewCount(0);
        }
        userAdjustmentMapper.insert(userAdjustment);
        return userAdjustment.getId();
    }

    @Override
    public void updateUserAdjustment(Long userId, AppUserAdjustmentSaveReqVO updateReqVO) {
        validatePublisherApproved(userId);
        UserAdjustmentDO existing = validateUserAdjustmentExists(updateReqVO.getId());
        if (!userId.equals(existing.getUserId())) {
            throw exception(USER_ADJUSTMENT_NOT_EXISTS);
        }
        UserAdjustmentDO updateObj = BeanUtils.toBean(updateReqVO, UserAdjustmentDO.class);
        updateObj.setUserId(existing.getUserId());
        updateObj.setPublishTime(existing.getPublishTime());
        updateObj.setViewCount(existing.getViewCount());
        userAdjustmentMapper.updateById(updateObj);
    }

    @Override
    public PageResult<AppUserAdjustmentListRespVO> getUserAdjustmentPublicPage(AppUserAdjustmentPageReqVO pageReqVO) {
        // only show open records by default
        if (pageReqVO.getStatus() == null) {
            pageReqVO.setStatus(1);
        }
        PageResult<UserAdjustmentDO> pageResult = userAdjustmentMapper.selectPage(pageReqVO);
        List<AppUserAdjustmentListRespVO> list = new ArrayList<>();
        for (UserAdjustmentDO item : pageResult.getList()) {
            list.add(toListResp(item));
        }
        return new PageResult<>(list, pageResult.getTotal());
    }

    @Override
    public PageResult<AppUserAdjustmentListRespVO> getMyUserAdjustmentPage(Long userId, AppUserAdjustmentPageReqVO pageReqVO) {
        pageReqVO.setUserId(userId);
        PageResult<UserAdjustmentDO> pageResult = userAdjustmentMapper.selectPage(pageReqVO);
        List<AppUserAdjustmentListRespVO> list = new ArrayList<>();
        for (UserAdjustmentDO item : pageResult.getList()) {
            list.add(toListResp(item));
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
        respVO.setQuotaText(buildQuotaText(userAdjustment));
        if (!canViewContact) {
            respVO.setContact(maskContact(userAdjustment.getContact()) + "（申请调剂后可查看）");
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

    private AppUserAdjustmentListRespVO toListResp(UserAdjustmentDO item) {
        AppUserAdjustmentListRespVO vo = new AppUserAdjustmentListRespVO();
        vo.setId(item.getId());
        vo.setTitle(item.getTitle());
        vo.setSchoolName(item.getSchoolName());
        vo.setMajorCode(item.getMajorCode());
        vo.setMajorName(item.getMajorName());
        vo.setPublishTime(item.getPublishTime());
        vo.setQuotaText(buildQuotaText(item));
        return vo;
    }

    private String buildQuotaText(UserAdjustmentDO item) {
        Integer left = item.getAdjustLeft();
        Integer count = item.getAdjustCount();
        if ((left == null || left <= 0) && (count == null || count <= 0)) {
            return "暂不确定";
        }
        // prefer adjustLeft for "招生人数"
        int v = (left != null && left > 0) ? left : (count != null ? count : 0);
        return String.valueOf(v);
    }

    private String maskContact(String contact) {
        if (StrUtil.isBlank(contact)) {
            return "******";
        }
        String s = contact.trim();
        // phone 11 digits
        if (s.matches("^1\\d{10}$")) {
            return s.substring(0, 3) + "****" + s.substring(7);
        }
        if (s.length() <= 2) {
            return "*";
        }
        String prefix = s.substring(0, Math.min(2, s.length()));
        String suffix = s.substring(Math.max(s.length() - 2, 0));
        return prefix + "****" + suffix;
    }

}