package com.hongguoyan.module.biz.service.vip;

import com.hongguoyan.module.biz.controller.app.vip.vo.AppVipCouponRedeemReqVO;
import com.hongguoyan.module.biz.controller.app.vip.vo.AppVipMeRespVO;
import com.hongguoyan.module.biz.controller.app.vip.vo.AppVipOrderCreateReqVO;
import com.hongguoyan.module.biz.controller.app.vip.vo.AppVipOrderCreateRespVO;
import com.hongguoyan.module.biz.controller.app.vip.vo.AppVipOrderPageReqVO;
import com.hongguoyan.module.biz.controller.app.vip.vo.AppVipOrderRespVO;
import com.hongguoyan.module.biz.controller.app.vip.vo.AppVipPlanRespVO;

import jakarta.validation.Valid;
import com.hongguoyan.framework.common.pojo.PageResult;
import java.util.List;

/**
 * 用户 APP - 会员 Service 接口
 */
public interface VipAppService {

    /**
     * 获得可售套餐列表（含权益点展示）
     *
     * @return 套餐列表
     */
    List<AppVipPlanRespVO> getPlanList();

    /**
     * 获得我的会员信息（若 userId 为空，返回默认值）
     *
     * @param userId 用户 ID
     * @return 我的会员信息
     */
    AppVipMeRespVO getMyVipInfo(Long userId);

    /**
     * 创建会员订单（暂不接支付）
     *
     * @param userId 用户 ID
     * @param reqVO 请求
     * @return 创建结果
     */
    AppVipOrderCreateRespVO createOrder(Long userId, @Valid AppVipOrderCreateReqVO reqVO);

    /**
     * 券码兑换并续期/开通订阅
     *
     * @param userId 用户 ID
     * @param reqVO 请求
     * @return 兑换后我的会员信息
     */
    AppVipMeRespVO redeemCoupon(Long userId, @Valid AppVipCouponRedeemReqVO reqVO);

    /**
     * 我的订单分页
     *
     * @param userId 用户 ID
     * @param reqVO 分页请求
     * @return 订单分页
     */
    PageResult<AppVipOrderRespVO> getMyOrderPage(Long userId, @Valid AppVipOrderPageReqVO reqVO);

}

