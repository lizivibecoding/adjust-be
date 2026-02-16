package com.hongguoyan.module.biz.enums.banner;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BannerPositionEnum {

    HOME(1, "首页"),
    CUSTOM(2, "定制页");

    private final Integer code;
    private final String desc;
}

