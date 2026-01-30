package com.hongguoyan.module.biz.service.useradjustment;

import jakarta.validation.*;
import com.hongguoyan.module.biz.controller.app.useradjustment.vo.*;
import com.hongguoyan.framework.common.pojo.PageResult;

/**
 * 用户发布调剂 Service 接口
 *
 * @author hgy
 */
public interface UserAdjustmentService {
    // ====== App business methods ======

    /**
     * 创建用户发布调剂
     */
    Long createUserAdjustment(Long userId, @Valid AppUserAdjustmentSaveReqVO createReqVO);

    /**
     * 更新用户发布调剂
     */
    void updateUserAdjustment(Long userId, @Valid AppUserAdjustmentSaveReqVO updateReqVO);

    /**
     * 发布调剂列表(公开)
     */
    PageResult<AppUserAdjustmentListRespVO> getUserAdjustmentPublicPage(AppUserAdjustmentPageReqVO pageReqVO);

    /**
     * 我发布的调剂分页
     */
    PageResult<AppUserAdjustmentListRespVO> getMyUserAdjustmentPage(Long userId, AppUserAdjustmentPageReqVO pageReqVO);

    /**
     * 调剂详情（含联系方式脱敏逻辑）
     */
    AppUserAdjustmentDetailRespVO getUserAdjustmentDetail(Long id, Long viewerUserId);

}