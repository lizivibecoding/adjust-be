package com.hongguoyan.module.biz.controller.app.vip.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "开放专业门类-响应")
@Data
public class AppVipMajorCategoryOpenRespVO {

    @Schema(description = "本次是否实际消耗额度（true=首次开通；false=已开通幂等）", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean consumed;

    @Schema(description = "提示信息", requiredMode = Schema.RequiredMode.REQUIRED, example = "已开通")
    private String message;
}

