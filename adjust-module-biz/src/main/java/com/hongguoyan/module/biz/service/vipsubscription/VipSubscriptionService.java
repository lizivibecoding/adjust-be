package com.hongguoyan.module.biz.service.vipsubscription;

import java.util.*;
import jakarta.validation.*;
import com.hongguoyan.module.biz.controller.admin.vipsubscription.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.vipsubscription.VipSubscriptionDO;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.pojo.PageParam;

/**
 * 用户会员订阅 Service 接口
 *
 * @author hgy
 */
public interface VipSubscriptionService {

    /**
     * 创建用户会员订阅
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createVipSubscription(@Valid VipSubscriptionSaveReqVO createReqVO);

    /**
     * 更新用户会员订阅
     *
     * @param updateReqVO 更新信息
     */
    void updateVipSubscription(@Valid VipSubscriptionSaveReqVO updateReqVO);

    /**
     * 删除用户会员订阅
     *
     * @param id 编号
     */
    void deleteVipSubscription(Long id);

    /**
    * 批量删除用户会员订阅
    *
    * @param ids 编号
    */
    void deleteVipSubscriptionListByIds(List<Long> ids);

    /**
     * 获得用户会员订阅
     *
     * @param id 编号
     * @return 用户会员订阅
     */
    VipSubscriptionDO getVipSubscription(Long id);

    /**
     * 获得用户会员订阅分页
     *
     * @param pageReqVO 分页查询
     * @return 用户会员订阅分页
     */
    PageResult<VipSubscriptionDO> getVipSubscriptionPage(VipSubscriptionPageReqVO pageReqVO);

}