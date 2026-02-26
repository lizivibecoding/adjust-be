package com.hongguoyan.module.biz.controller.app.adjustment.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "调剂更新统计-响应")
@Data
public class AppAdjustmentUpdateStatsRespVO {

    @Schema(description = "年份", example = "2025")
    private Integer year;

    @Schema(description = "最近更新时间")
    private LocalDateTime lastUpdateTime;

    @Schema(description = "今日更新数据量", example = "123")
    private Long todayUpdateCount;

    @Schema(description = "今年更新数据量(自然年)", example = "123")
    private Long thisYearUpdateCount;

    @Schema(description = "去年更新数据量(自然年)", example = "123")
    private Long lastYearUpdateCount;

    @Schema(description = "历史更新数据量(包含去年及更早，自然年)", example = "123")
    private Long historyUpdateCount;

}

