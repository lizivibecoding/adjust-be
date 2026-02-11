package com.hongguoyan.module.biz.enums.publisher;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PublisherAuditActionEnum {

    SUBMIT(1, "提交"),
    APPROVE(2, "通过"),
    REJECT(3, "拒绝"),
    DISABLE(4, "禁用"),
    ENABLE(5, "启用");

    private final Integer code;
    private final String desc;
}
