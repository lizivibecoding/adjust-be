package com.hongguoyan.module.biz.controller.app.useradjustmentapply.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "用户 APP - 申请人列表 Item Response VO")
@Data
public class AppUserAdjustmentApplicantListItemRespVO {

    @Schema(description = "申请ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Schema(description = "申请人姓名")
    private String candidateName;

    @Schema(description = "申请时间")
    private LocalDateTime applyTime;
}

