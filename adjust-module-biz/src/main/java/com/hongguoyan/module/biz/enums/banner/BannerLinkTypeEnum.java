package com.hongguoyan.module.biz.enums.banner;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BannerLinkTypeEnum {

    NONE(0, "无"),
    H5(1, "H5"),
    MINI_PROGRAM_PAGE(2, "小程序页面");

    private final Integer code;
    private final String desc;
}

