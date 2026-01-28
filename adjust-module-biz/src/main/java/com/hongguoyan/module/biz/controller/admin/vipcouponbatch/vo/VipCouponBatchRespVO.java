package com.hongguoyan.module.biz.controller.admin.vipcouponbatch.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import cn.idev.excel.annotation.*;

@Schema(description = "管理后台 - 会员券码批次 Response VO")
@Data
@ExcelIgnoreUnannotated
public class VipCouponBatchRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "9574")
    @ExcelProperty("主键")
    private Long id;

    @Schema(description = "批次号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("批次号")
    private String batchNo;

    @Schema(description = "套餐编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("套餐编码")
    private String planCode;

    @Schema(description = "开始时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("开始时间")
    private LocalDateTime validStartTime;

    @Schema(description = "截止时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("截止时间")
    private LocalDateTime validEndTime;

    @Schema(description = "总数", requiredMode = Schema.RequiredMode.REQUIRED, example = "26531")
    @ExcelProperty("总数")
    private Integer totalCount;

    @Schema(description = "已使用数", requiredMode = Schema.RequiredMode.REQUIRED, example = "11422")
    @ExcelProperty("已使用数")
    private Integer usedCount;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("状态")
    private Integer status;

    @Schema(description = "备注", example = "你猜")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}