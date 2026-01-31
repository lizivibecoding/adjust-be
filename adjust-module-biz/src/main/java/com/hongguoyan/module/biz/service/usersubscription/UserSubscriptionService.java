package com.hongguoyan.module.biz.service.usersubscription;

import java.util.*;
import jakarta.validation.*;
import com.hongguoyan.module.biz.controller.app.usersubscription.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.usersubscription.UserSubscriptionDO;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.pojo.PageParam;

/**
 * 用户调剂订阅 Service 接口
 *
 * @author hgy
 */
public interface UserSubscriptionService {

    /**
     * 创建用户调剂订阅
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createUserSubscription(@Valid AppUserSubscriptionSaveReqVO createReqVO);

    /**
     * 更新用户调剂订阅
     *
     * @param updateReqVO 更新信息
     */
    void updateUserSubscription(@Valid AppUserSubscriptionSaveReqVO updateReqVO);

    /**
     * 删除用户调剂订阅
     *
     * @param id 编号
     */
    void deleteUserSubscription(Long id);

    /**
    * 批量删除用户调剂订阅
    *
    * @param ids 编号
    */
    void deleteUserSubscriptionListByIds(List<Long> ids);

    /**
     * 获得用户调剂订阅
     *
     * @param id 编号
     * @return 用户调剂订阅
     */
    UserSubscriptionDO getUserSubscription(Long id);

    /**
     * 获得用户调剂订阅分页
     *
     * @param pageReqVO 分页查询
     * @return 用户调剂订阅分页
     */
    PageResult<UserSubscriptionDO> getUserSubscriptionPage(AppUserSubscriptionPageReqVO pageReqVO);

}