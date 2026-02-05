package com.hongguoyan.module.biz.controller.app.vip.vo;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.hongguoyan.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static com.hongguoyan.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "用户权益用量汇总-分页-请求")
@Data
public class AppVipBenefitUsagePageReqVO extends PageParam {

    @Schema(description = "用户ID", example = "30145")
    private Long userId;

    @Schema(description = "权益 key")
    private String benefitKey;

    @Schema(description = "周期开始时间（按 period_type 对齐）")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] periodStartTime;

    @Schema(description = "周期结束时间（建议为开区间终点）")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] periodEndTime;

    @Schema(description = "周期内已用次数", example = "969")
    private Integer usedCount;

    @Schema(description = "最近一次消耗时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] lastUsedTime;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}