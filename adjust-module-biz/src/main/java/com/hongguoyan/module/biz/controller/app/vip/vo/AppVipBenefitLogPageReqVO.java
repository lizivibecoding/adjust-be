package com.hongguoyan.module.biz.controller.app.vip.vo;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.hongguoyan.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static com.hongguoyan.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "用户 APP - 用户权益消耗明细分页 Request VO")
@Data
public class AppVipBenefitLogPageReqVO extends PageParam {

    @Schema(description = "用户ID", example = "24691")
    private Long userId;

    @Schema(description = "权益 key")
    private String benefitKey;

    @Schema(description = "周期开始时间（按 period_type 对齐）")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] periodStartTime;

    @Schema(description = "周期结束时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] periodEndTime;

    @Schema(description = "本次消耗次数（一般为 1）", example = "30668")
    private Integer consumeCount;

    @Schema(description = "关联类型（如 CUSTOM_REPORT/VOLUNTEER_EXPORT/SUBJECT_CATEGORY_OPEN）", example = "2")
    private String refType;

    @Schema(description = "关联ID（报告ID/导出ID等）", example = "32327")
    private String refId;

    @Schema(description = "去重键（consume_policy=UNIQUE_KEY 时使用）")
    private String uniqueKey;

    @Schema(description = "备注", example = "随便")
    private String remark;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}