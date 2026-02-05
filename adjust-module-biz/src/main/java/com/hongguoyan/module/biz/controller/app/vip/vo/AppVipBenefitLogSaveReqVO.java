package com.hongguoyan.module.biz.controller.app.vip.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

@Schema(description = "用户权益消耗明细-保存-请求")
@Data
public class AppVipBenefitLogSaveReqVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "29831")
    private Long id;

    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "24691")
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @Schema(description = "权益 key", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "权益 key不能为空")
    private String benefitKey;

    @Schema(description = "周期开始时间（按 period_type 对齐）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "周期开始时间（按 period_type 对齐）不能为空")
    private LocalDateTime periodStartTime;

    @Schema(description = "周期结束时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "周期结束时间不能为空")
    private LocalDateTime periodEndTime;

    @Schema(description = "本次消耗次数（一般为 1）", requiredMode = Schema.RequiredMode.REQUIRED, example = "30668")
    @NotNull(message = "本次消耗次数（一般为 1）不能为空")
    private Integer consumeCount;

    @Schema(description = "关联类型（如 CUSTOM_REPORT/VOLUNTEER_EXPORT/SUBJECT_CATEGORY_OPEN）", example = "2")
    private String refType;

    @Schema(description = "关联ID（报告ID/导出ID等）", example = "32327")
    private String refId;

    @Schema(description = "去重键（consume_policy=UNIQUE_KEY 时使用）")
    private String uniqueKey;

    @Schema(description = "备注", example = "随便")
    private String remark;

}