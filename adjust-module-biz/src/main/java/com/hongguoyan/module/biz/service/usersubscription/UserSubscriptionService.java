package com.hongguoyan.module.biz.service.usersubscription;

import jakarta.validation.*;
import com.hongguoyan.module.biz.controller.app.usersubscription.vo.*;
import com.hongguoyan.framework.common.pojo.PageResult;

/**
 * 用户调剂订阅 Service 接口
 *
 * @author hgy
 */
public interface UserSubscriptionService {

    /**
     * 订阅
     */
    void subscribe(Long userId, @Valid AppUserSubscriptionSubscribeReqVO reqVO);

    /**
     * 取消订阅
     */
    Boolean unsubscribe(Long userId, @Valid AppUserSubscriptionUnsubscribeReqVO reqVO);

    /**
     * 我的订阅(按学校分组)
     */
    PageResult<AppUserSubscriptionPageRespVO> getMyPage(Long userId, @Valid AppUserSubscriptionPageReqVO reqVO);

}