package com.hongguoyan.module.biz.controller.admin.publisher.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "管理后台 - 发布者资质审核动作 Request VO")
@Data
public class PublisherAuditReqVO {

    @Schema(description = "发布者资质 ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "发布者资质 ID 不能为空")
    private Long id;

    @Schema(description = "原因/备注（拒绝、禁用时建议填写）")
    private String reason;
}
