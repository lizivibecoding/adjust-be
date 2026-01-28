package com.hongguoyan.module.biz.controller.app.banner.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "用户 APP - 轮播图列表 Request VO")
@Data
public class AppBannerListReqVO {

    @Schema(description = "展示位置(1=首页,2=定制页)", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "展示位置不能为空")
    private Integer position;

}

