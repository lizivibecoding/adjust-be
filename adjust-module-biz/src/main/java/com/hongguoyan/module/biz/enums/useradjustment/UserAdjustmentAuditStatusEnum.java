package com.hongguoyan.module.biz.enums.useradjustment;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户发布调剂 - 审核状态
 */
@Getter
@AllArgsConstructor
public enum UserAdjustmentAuditStatusEnum {

    PENDING(0, "待审"),
    APPROVED(1, "通过"),
    REJECTED(2, "拒绝");

    private final Integer code;
    private final String desc;
}

