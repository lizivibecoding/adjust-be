package com.hongguoyan.module.biz.controller.app.vip.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "用户 APP - 我的权益明细 Item Response VO")
@Data
public class AppVipBenefitMeItemRespVO {

    @Schema(description = "权益 key", example = "user_report")
    private String benefitKey;

    @Schema(description = "权益类型：1=BOOLEAN 2=QUOTA 3=LIMIT 4=RESOURCE", example = "2")
    private Integer benefitType;

    @Schema(description = "权益数值（如 1/3/8；-1=不限）", example = "1")
    private Integer benefitValue;

    @Schema(description = "周期：0=NONE 1=DAY 2=WEEK 3=MONTH 4=YEAR 9=LIFETIME", example = "9")
    private Integer periodType;

    @Schema(description = "计次策略：1=COUNT 2=UNIQUE_KEY", example = "1")
    private Integer consumePolicy;

    @Schema(description = "是否可用（合并结果）", example = "true")
    private Boolean enabled;

    @Schema(description = "当前周期开始时间")
    private LocalDateTime periodStartTime;

    @Schema(description = "当前周期结束时间（开区间终点）")
    private LocalDateTime periodEndTime;

    @Schema(description = "当前周期已用次数（QUOTA）", example = "0")
    private Integer usedCount;

    @Schema(description = "当前周期剩余次数（QUOTA）；-1 表示不限", example = "1")
    private Integer remainingCount;
}

