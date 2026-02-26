package com.hongguoyan.module.biz.service.useradjustment;

import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.module.biz.controller.admin.useradjustment.vo.UserAdjustmentAdminAuditReqVO;
import com.hongguoyan.module.biz.controller.admin.useradjustment.vo.UserAdjustmentAdminCreateReqVO;
import com.hongguoyan.module.biz.controller.admin.useradjustment.vo.UserAdjustmentAdminPageReqVO;
import com.hongguoyan.module.biz.controller.admin.useradjustment.vo.UserAdjustmentAdminPageRespVO;

/**
 * 用户发布调剂 - 管理后台 Service
 */
public interface UserAdjustmentAdminService {

    PageResult<UserAdjustmentAdminPageRespVO> getApprovedPage(UserAdjustmentAdminPageReqVO reqVO);

    PageResult<UserAdjustmentAdminPageRespVO> getAuditPage(UserAdjustmentAdminPageReqVO reqVO);

    Long createByAdmin(Long adminUserId, UserAdjustmentAdminCreateReqVO reqVO);

    void approve(Long adminUserId, UserAdjustmentAdminAuditReqVO reqVO);

    void reject(Long adminUserId, UserAdjustmentAdminAuditReqVO reqVO);
}

