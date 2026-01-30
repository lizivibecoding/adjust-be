package com.hongguoyan.module.biz.service.useradjustmentapply;

import java.util.*;
import jakarta.validation.*;
import com.hongguoyan.module.biz.controller.app.useradjustmentapply.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.useradjustmentapply.UserAdjustmentApplyDO;
import com.hongguoyan.framework.common.pojo.PageResult;

/**
 * 用户发布调剂申请记录 Service 接口
 *
 * @author hgy
 */
public interface UserAdjustmentApplyService {

    // ====== Generated CRUD (for admin reuse) ======

    Long createUserAdjustmentApply(@Valid AppUserAdjustmentApplySaveReqVO createReqVO);

    void updateUserAdjustmentApply(@Valid AppUserAdjustmentApplySaveReqVO updateReqVO);

    void deleteUserAdjustmentApply(Long id);

    void deleteUserAdjustmentApplyListByIds(List<Long> ids);

    UserAdjustmentApplyDO getUserAdjustmentApply(Long id);

    PageResult<UserAdjustmentApplyDO> getUserAdjustmentApplyPage(AppUserAdjustmentApplyPageReqVO pageReqVO);

    // ====== App business methods ======

    /**
     * 创建用户发布调剂申请记录
     */
    Long createUserAdjustmentApply(Long userId, @Valid AppUserAdjustmentApplySaveReqVO createReqVO);

    /**
     * 我申请的调剂分页
     */
    PageResult<AppUserAdjustmentApplyMyItemRespVO> getMyAppliedPage(Long userId, AppUserAdjustmentApplyPageReqVO pageReqVO);

    /**
     * 申请人列表(发布者查看)
     */
    List<AppUserAdjustmentApplicantListItemRespVO> getApplicantList(Long publisherUserId, Long userAdjustmentId);

    /**
     * 申请详情(发布者查看)
     */
    AppUserAdjustmentApplicantDetailRespVO getApplicantDetail(Long publisherUserId, Long applyId);

}