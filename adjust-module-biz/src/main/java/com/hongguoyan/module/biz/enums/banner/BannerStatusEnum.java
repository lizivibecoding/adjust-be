package com.hongguoyan.module.biz.enums.banner;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public enum BannerStatusEnum {

    DISABLE(0, "停用"),
    ENABLE(1, "启用");

    private final Integer code;
    private final String desc;

    public static boolean isEnable(Integer code) {
        return Objects.equals(code, ENABLE.code);
    }
}

