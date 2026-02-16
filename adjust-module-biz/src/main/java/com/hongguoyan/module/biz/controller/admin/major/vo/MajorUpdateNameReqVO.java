package com.hongguoyan.module.biz.controller.admin.major.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "管理后台 - 专业改名 Request VO")
@Data
public class MajorUpdateNameReqVO {

    @Schema(description = "专业ID(biz_major.id)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2810")
    @NotNull(message = "id 不能为空")
    private Long id;

    @Schema(description = "专业名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "计算机科学与技术")
    @NotBlank(message = "name 不能为空")
    private String name;
}

