package com.hongguoyan.module.biz.controller.app.useradjustmentapply.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "用户 APP - 我申请的调剂 Item Response VO")
@Data
public class AppUserAdjustmentApplyMyItemRespVO {

    @Schema(description = "用户发布调剂ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long userAdjustmentId;

    @Schema(description = "调剂信息标题")
    private String title;

    @Schema(description = "调剂专业展示文本")
    private String majorText;

    @Schema(description = "申请时间")
    private LocalDateTime applyTime;
}

