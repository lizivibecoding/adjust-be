package com.hongguoyan.module.biz.controller.app.adjustment.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "用户 APP - 调剂更新统计 Response VO")
@Data
public class AppAdjustmentUpdateStatsRespVO {

    @Schema(description = "年份", example = "2025")
    private Integer year;

    @Schema(description = "最近更新时间")
    private LocalDateTime lastUpdateTime;

    @Schema(description = "今日更新数据量", example = "123")
    private Long todayUpdateCount;

    @Schema(description = "今日更新院校数(去重)", example = "45")
    private Long todayUpdateSchoolCount;

    @Schema(description = "累计数据量", example = "26266")
    private Long totalCount;

    @Schema(description = "累计院校数(去重)", example = "791")
    private Long totalSchoolCount;

}

