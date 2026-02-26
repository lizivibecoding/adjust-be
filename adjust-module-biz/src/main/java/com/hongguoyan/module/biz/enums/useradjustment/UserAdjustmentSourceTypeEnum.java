package com.hongguoyan.module.biz.enums.useradjustment;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户发布调剂 - 发布来源
 */
@Getter
@AllArgsConstructor
public enum UserAdjustmentSourceTypeEnum {

    TEACHER(1, "老师发布"),
    SENIOR(2, "学长发布"),
    RUMOR(3, "小道消息");

    private final Integer code;
    private final String desc;
}

