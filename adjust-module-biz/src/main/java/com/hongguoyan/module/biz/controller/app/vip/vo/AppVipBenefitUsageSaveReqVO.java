package com.hongguoyan.module.biz.controller.app.vip.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

@Schema(description = "用户 APP - 用户权益用量汇总新增/修改 Request VO")
@Data
public class AppVipBenefitUsageSaveReqVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "4958")
    private Long id;

    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "30145")
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @Schema(description = "权益 key", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "权益 key不能为空")
    private String benefitKey;

    @Schema(description = "周期开始时间（按 period_type 对齐）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "周期开始时间（按 period_type 对齐）不能为空")
    private LocalDateTime periodStartTime;

    @Schema(description = "周期结束时间（建议为开区间终点）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "周期结束时间（建议为开区间终点）不能为空")
    private LocalDateTime periodEndTime;

    @Schema(description = "周期内已用次数", requiredMode = Schema.RequiredMode.REQUIRED, example = "969")
    @NotNull(message = "周期内已用次数不能为空")
    private Integer usedCount;

    @Schema(description = "最近一次消耗时间")
    private LocalDateTime lastUsedTime;

}