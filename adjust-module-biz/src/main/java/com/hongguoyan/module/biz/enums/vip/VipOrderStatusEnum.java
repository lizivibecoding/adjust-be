package com.hongguoyan.module.biz.enums.vip;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public enum VipOrderStatusEnum {

    WAIT_PAY(1, "待支付"),
    PAID(2, "已支付"),
    EXPIRED(3, "已过期"),
    REFUNDED(4, "已退款"),
    CANCELED(5, "已取消");

    private final Integer code;
    private final String desc;

    public static boolean isPaid(Integer code) {
        return Objects.equals(code, PAID.code);
    }

    public static boolean isWaitPay(Integer code) {
        return Objects.equals(code, WAIT_PAY.code);
    }

    public static boolean isRefunded(Integer code) {
        return Objects.equals(code, REFUNDED.code);
    }
}
