package com.hongguoyan.module.biz.controller.admin.vipcouponcode.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import cn.idev.excel.annotation.*;

@Schema(description = "管理后台 - 会员券码 Response VO")
@Data
@ExcelIgnoreUnannotated
public class VipCouponCodeRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "31822")
    @ExcelProperty("主键")
    private Long id;

    @Schema(description = "券码", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("券码")
    private String code;

    @Schema(description = "批次号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("批次号")
    private String batchNo;

    @Schema(description = "套餐编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("套餐编码")
    private String planCode;

    @Schema(description = "状态：1未使用,2已使用,3过期,4作废", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("状态：1未使用,2已使用,3过期,4作废")
    private Integer status;

    @Schema(description = "开始时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("开始时间")
    private LocalDateTime validStartTime;

    @Schema(description = "截止时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("截止时间")
    private LocalDateTime validEndTime;

    @Schema(description = "使用人ID", example = "27020")
    @ExcelProperty("使用人ID")
    private Long userId;

    @Schema(description = "使用时间")
    @ExcelProperty("使用时间")
    private LocalDateTime usedTime;

    @Schema(description = "备注", example = "你说的对")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}