package com.hongguoyan.module.biz.job.vip;

import cn.hutool.core.util.StrUtil;
import com.hongguoyan.framework.quartz.core.handler.JobHandler;
import com.hongguoyan.module.biz.service.viporder.VipOrderService;
import com.hongguoyan.module.pay.service.order.PayOrderService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * 会员订单和支付订单过期处理 Job
 */
@Component
public class VipOrderExpireJob implements JobHandler {

    @Resource
    private VipOrderService vipOrderService;
    @Resource
    private PayOrderService payOrderService;

    @Override
    public String execute(String param) {
        int vipCount = vipOrderService.expireOrder();
        int payCount = payOrderService.expireOrder();
        return StrUtil.format("会员过期 {} 个，支付过期 {} 个", vipCount, payCount);
    }
}
