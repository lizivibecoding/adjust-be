package com.hongguoyan.module.biz.service.useradjustment;

import java.util.*;
import jakarta.validation.*;
import com.hongguoyan.module.biz.controller.app.useradjustment.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.useradjustment.UserAdjustmentDO;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.pojo.PageParam;

/**
 * 用户发布调剂 Service 接口
 *
 * @author hgy
 */
public interface UserAdjustmentService {

    /**
     * 创建用户发布调剂
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createUserAdjustment(@Valid AppUserAdjustmentSaveReqVO createReqVO);

    /**
     * 更新用户发布调剂
     *
     * @param updateReqVO 更新信息
     */
    void updateUserAdjustment(@Valid AppUserAdjustmentSaveReqVO updateReqVO);

    /**
     * 删除用户发布调剂
     *
     * @param id 编号
     */
    void deleteUserAdjustment(Long id);

    /**
    * 批量删除用户发布调剂
    *
    * @param ids 编号
    */
    void deleteUserAdjustmentListByIds(List<Long> ids);

    /**
     * 获得用户发布调剂
     *
     * @param id 编号
     * @return 用户发布调剂
     */
    UserAdjustmentDO getUserAdjustment(Long id);

    /**
     * 获得用户发布调剂分页
     *
     * @param pageReqVO 分页查询
     * @return 用户发布调剂分页
     */
    PageResult<UserAdjustmentDO> getUserAdjustmentPage(AppUserAdjustmentPageReqVO pageReqVO);

}