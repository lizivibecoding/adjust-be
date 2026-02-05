package com.hongguoyan.module.biz.controller.app.recommend.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Schema(description = "用户 APP - 修改报告名称 Request VO")
@Data
public class AppUserCustomReportRenameReqVO {

    @Schema(description = "报告ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "11001")
    @NotNull(message = "报告ID不能为空")
    private Long reportId;

    @Schema(description = "报告名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "20260101 - 调剂报告 - 01")
    @NotBlank(message = "报告名称不能为空")
    @Size(max = 64, message = "报告名称不能超过 64 个字符")
    private String reportName;

}

