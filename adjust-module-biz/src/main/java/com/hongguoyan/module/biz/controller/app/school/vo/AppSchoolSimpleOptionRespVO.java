package com.hongguoyan.module.biz.controller.app.school.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "用户 APP - 学校简单选项 Response VO")
@Data
public class AppSchoolSimpleOptionRespVO {

    @Schema(description = "学校ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "9101")
    private Long id;

    @Schema(description = "学校名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "北京理工大学")
    private String name;
}

