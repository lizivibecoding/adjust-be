package com.hongguoyan.module.biz.job.vip;

import cn.hutool.core.util.StrUtil;
import com.hongguoyan.framework.quartz.core.handler.JobHandler;
import com.hongguoyan.module.biz.service.vipcoupon.VipCouponService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * 会员券码过期处理 Job
 */
@Component
public class VipCouponExpireJob implements JobHandler {

    @Resource
    private VipCouponService vipCouponService;

    @Override
    public String execute(String param) {
        int count = vipCouponService.expireCoupon();
        return StrUtil.format("券码过期 {} 个", count);
    }
}

