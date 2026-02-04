package com.hongguoyan.module.biz.controller.app.vip.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import cn.idev.excel.annotation.*;

@Schema(description = "用户 APP - 用户权益消耗明细 Response VO")
@Data
@ExcelIgnoreUnannotated
public class AppVipBenefitLogRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "29831")
    @ExcelProperty("主键")
    private Long id;

    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "24691")
    @ExcelProperty("用户ID")
    private Long userId;

    @Schema(description = "权益 key", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("权益 key")
    private String benefitKey;

    @Schema(description = "周期开始时间（按 period_type 对齐）", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("周期开始时间（按 period_type 对齐）")
    private LocalDateTime periodStartTime;

    @Schema(description = "周期结束时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("周期结束时间")
    private LocalDateTime periodEndTime;

    @Schema(description = "本次消耗次数（一般为 1）", requiredMode = Schema.RequiredMode.REQUIRED, example = "30668")
    @ExcelProperty("本次消耗次数（一般为 1）")
    private Integer consumeCount;

    @Schema(description = "关联类型（如 CUSTOM_REPORT/VOLUNTEER_EXPORT/SUBJECT_CATEGORY_OPEN）", example = "2")
    @ExcelProperty("关联类型（如 CUSTOM_REPORT/VOLUNTEER_EXPORT/SUBJECT_CATEGORY_OPEN）")
    private String refType;

    @Schema(description = "关联ID（报告ID/导出ID等）", example = "32327")
    @ExcelProperty("关联ID（报告ID/导出ID等）")
    private String refId;

    @Schema(description = "去重键（consume_policy=UNIQUE_KEY 时使用）")
    @ExcelProperty("去重键（consume_policy=UNIQUE_KEY 时使用）")
    private String uniqueKey;

    @Schema(description = "备注", example = "随便")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}