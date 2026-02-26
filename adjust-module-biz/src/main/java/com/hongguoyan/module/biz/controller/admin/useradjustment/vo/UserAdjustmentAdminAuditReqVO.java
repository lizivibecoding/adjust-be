package com.hongguoyan.module.biz.controller.admin.useradjustment.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "管理后台 - 用户发布调剂审核 Request VO")
@Data
public class UserAdjustmentAdminAuditReqVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "id不能为空")
    private Long id;

    @Schema(description = "原因（拒绝时必填）")
    private String reason;
}

