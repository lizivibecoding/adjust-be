package com.hongguoyan.module.biz.service.viporder;

import java.util.*;
import com.hongguoyan.module.biz.controller.admin.viporder.vo.*;
import com.hongguoyan.framework.common.pojo.PageResult;

/**
 * 会员订单 Service 接口
 *
 * @author hgy
 */
public interface VipOrderService {

    /**
     * 获得会员订单
     *
     * @param id 编号
     * @return 会员订单
     */
    VipOrderRespVO getVipOrder(Long id);

    /**
     * 获得会员订单分页
     *
     * @param pageReqVO 分页查询
     * @return 会员订单分页
     */
    PageResult<VipOrderRespVO> getVipOrderPage(VipOrderPageReqVO pageReqVO);

    /**
     * 关闭已过期的待支付会员订单
     *
     * @return 处理数量
     */
    int expireOrder();

    /**
     * 后台发起退款：按用户与套餐，退最近一笔已支付且未发起退款的订单（LIFO）
     */
    VipOrderRefundRespVO refundLatestPaidOrder(VipOrderRefundReqVO reqVO);

}