package com.hongguoyan.module.biz.enums.vip;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public enum VipCouponStatusEnum {

    UNUSED(1, "未使用"),
    USED(2, "已使用"),
    EXPIRED(3, "已过期"),
    INVALID(4, "作废");

    private final Integer code;
    private final String desc;

    public static boolean isUnused(Integer code) {
        return Objects.equals(code, UNUSED.code);
    }

    public static boolean isUsed(Integer code) {
        return Objects.equals(code, USED.code);
    }
}

