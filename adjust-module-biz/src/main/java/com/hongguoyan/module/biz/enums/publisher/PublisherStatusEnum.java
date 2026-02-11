package com.hongguoyan.module.biz.enums.publisher;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PublisherStatusEnum {

    PENDING(0, "待审"),
    APPROVED(1, "通过"),
    REJECTED(2, "拒绝"),
    DISABLED(3, "禁用");

    private final Integer code;
    private final String desc;
}
