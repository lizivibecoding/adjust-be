package com.hongguoyan.module.biz.service.vipsubscription;

import com.hongguoyan.module.biz.controller.admin.vipsubscription.vo.*;
import com.hongguoyan.framework.common.pojo.PageResult;

/**
 * 用户会员订阅 Service 接口
 *
 * @author hgy
 */
public interface VipSubscriptionService {

    /**
     * 获得用户会员订阅
     *
     * @param id 编号
     * @return 用户会员订阅
     */
    VipSubscriptionRespVO getVipSubscription(Long id);

    /**
     * 获得用户会员订阅分页
     *
     * @param pageReqVO 分页查询
     * @return 用户会员订阅分页
     */
    PageResult<VipSubscriptionRespVO> getVipSubscriptionPage(VipSubscriptionPageReqVO pageReqVO);

    /**
     * 获得用户会员订阅统计
     *
     * @return 统计数据
     */
    VipSubscriptionSummaryRespVO getVipSubscriptionSummary();

}