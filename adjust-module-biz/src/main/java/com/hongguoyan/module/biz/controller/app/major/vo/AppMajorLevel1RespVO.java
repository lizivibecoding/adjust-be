package com.hongguoyan.module.biz.controller.app.major.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "一级学科-响应")
@Data
public class AppMajorLevel1RespVO {

    @Schema(description = "学科代码", example = "02")
    private String code;

    @Schema(description = "学科名称", example = "工学")
    private String name;
}
