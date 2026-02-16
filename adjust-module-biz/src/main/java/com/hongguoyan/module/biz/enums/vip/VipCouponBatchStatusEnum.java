package com.hongguoyan.module.biz.enums.vip;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum VipCouponBatchStatusEnum {

    ENABLE(1, "启用");

    private final Integer code;
    private final String desc;

}

