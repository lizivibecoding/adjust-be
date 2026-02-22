package com.hongguoyan.module.biz.controller.admin.vipplan.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hongguoyan.module.biz.framework.jackson.LocalDateTimeOrTimestampDeserializer;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 套餐卡片更新 Request VO")
@Data
public class VipPlanCardUpdateReqVO {

    @Schema(description = "套餐编码：VIP / SVIP", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "套餐编码不能为空")
    private String planCode;

    @Schema(description = "价格（单位：分）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "价格不能为空")
    @Min(value = 0, message = "价格不能小于 0")
    private Integer planPrice;

    @Schema(description = "周期（单位：天）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "周期不能为空")
    @Min(value = 1, message = "周期不能小于 1")
    private Integer durationDays;

    @Schema(description = "活动价（单位：分）")
    @Min(value = 0, message = "活动价不能小于 0")
    private Integer discountPrice;

    @Schema(description = "活动开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeOrTimestampDeserializer.class)
    private LocalDateTime discountStartTime;

    @Schema(description = "活动结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeOrTimestampDeserializer.class)
    private LocalDateTime discountEndTime;

    @Schema(description = "是否取消活动：true=清空活动价与起止时间")
    private Boolean cancelDiscount;

    @Schema(description = "开放专业门类次数（-1 表示不限）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "开放专业门类次数不能为空")
    @Min(value = -1, message = "开放专业门类次数不能小于 -1")
    private Integer majorCategoryOpenCount;

    @Schema(description = "AI 定制报告次数（-1 表示不限）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "AI 定制报告次数不能为空")
    @Min(value = -1, message = "AI 定制报告次数不能小于 -1")
    private Integer userReportCount;

}
