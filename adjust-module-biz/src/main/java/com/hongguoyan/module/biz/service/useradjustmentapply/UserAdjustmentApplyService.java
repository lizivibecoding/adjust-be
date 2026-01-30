package com.hongguoyan.module.biz.service.useradjustmentapply;

import java.util.*;
import jakarta.validation.*;
import com.hongguoyan.module.biz.controller.app.useradjustmentapply.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.useradjustmentapply.UserAdjustmentApplyDO;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.pojo.PageParam;

/**
 * 用户发布调剂申请记录 Service 接口
 *
 * @author hgy
 */
public interface UserAdjustmentApplyService {

    /**
     * 创建用户发布调剂申请记录
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createUserAdjustmentApply(@Valid AppUserAdjustmentApplySaveReqVO createReqVO);

    /**
     * 更新用户发布调剂申请记录
     *
     * @param updateReqVO 更新信息
     */
    void updateUserAdjustmentApply(@Valid AppUserAdjustmentApplySaveReqVO updateReqVO);

    /**
     * 删除用户发布调剂申请记录
     *
     * @param id 编号
     */
    void deleteUserAdjustmentApply(Long id);

    /**
    * 批量删除用户发布调剂申请记录
    *
    * @param ids 编号
    */
    void deleteUserAdjustmentApplyListByIds(List<Long> ids);

    /**
     * 获得用户发布调剂申请记录
     *
     * @param id 编号
     * @return 用户发布调剂申请记录
     */
    UserAdjustmentApplyDO getUserAdjustmentApply(Long id);

    /**
     * 获得用户发布调剂申请记录分页
     *
     * @param pageReqVO 分页查询
     * @return 用户发布调剂申请记录分页
     */
    PageResult<UserAdjustmentApplyDO> getUserAdjustmentApplyPage(AppUserAdjustmentApplyPageReqVO pageReqVO);

}