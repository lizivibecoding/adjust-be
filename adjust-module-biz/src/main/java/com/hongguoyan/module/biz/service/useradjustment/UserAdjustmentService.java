package com.hongguoyan.module.biz.service.useradjustment;

import java.util.*;
import jakarta.validation.*;
import com.hongguoyan.module.biz.controller.app.useradjustment.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.useradjustment.UserAdjustmentDO;
import com.hongguoyan.framework.common.pojo.PageResult;

/**
 * 用户发布调剂 Service 接口
 *
 * @author hgy
 */
public interface UserAdjustmentService {

    // ====== Generated CRUD (for admin reuse) ======

    Long createUserAdjustment(@Valid AppUserAdjustmentSaveReqVO createReqVO);

    void updateUserAdjustment(@Valid AppUserAdjustmentSaveReqVO updateReqVO);

    void deleteUserAdjustment(Long id);

    void deleteUserAdjustmentListByIds(List<Long> ids);

    UserAdjustmentDO getUserAdjustment(Long id);

    PageResult<UserAdjustmentDO> getUserAdjustmentPage(AppUserAdjustmentPageReqVO pageReqVO);

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