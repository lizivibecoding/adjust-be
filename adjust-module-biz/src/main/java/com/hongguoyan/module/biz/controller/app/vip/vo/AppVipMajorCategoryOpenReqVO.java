package com.hongguoyan.module.biz.controller.app.vip.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Schema(description = "开放专业门类-请求")
@Data
public class AppVipMajorCategoryOpenReqVO {

    @Schema(description = "专业门类代码", requiredMode = Schema.RequiredMode.REQUIRED, example = "08")
    @NotBlank(message = "专业门类代码不能为空")
    private String majorCode;
}

