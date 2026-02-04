package com.hongguoyan.module.biz.controller.app.vip.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import cn.idev.excel.annotation.*;

@Schema(description = "用户 APP - 用户权益用量汇总 Response VO")
@Data
@ExcelIgnoreUnannotated
public class AppVipBenefitUsageRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "4958")
    @ExcelProperty("主键")
    private Long id;

    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "30145")
    @ExcelProperty("用户ID")
    private Long userId;

    @Schema(description = "权益 key", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("权益 key")
    private String benefitKey;

    @Schema(description = "周期开始时间（按 period_type 对齐）", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("周期开始时间（按 period_type 对齐）")
    private LocalDateTime periodStartTime;

    @Schema(description = "周期结束时间（建议为开区间终点）", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("周期结束时间（建议为开区间终点）")
    private LocalDateTime periodEndTime;

    @Schema(description = "周期内已用次数", requiredMode = Schema.RequiredMode.REQUIRED, example = "969")
    @ExcelProperty("周期内已用次数")
    private Integer usedCount;

    @Schema(description = "最近一次消耗时间")
    @ExcelProperty("最近一次消耗时间")
    private LocalDateTime lastUsedTime;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}