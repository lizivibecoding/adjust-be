package com.hongguoyan.module.biz.controller.app.area.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "用户 APP - 省份/分区 Response VO")
@Data
public class AppAreaRespVO {

    @Schema(description = "省份 code (biz_area.code)", example = "31")
    private String code;

    @Schema(description = "省份名称", example = "上海")
    private String name;

    @Schema(description = "考研分区(A/B)", example = "A")
    private String area;
}

