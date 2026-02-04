package com.hongguoyan.module.biz.controller.admin.vipplanbenefit.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 套餐权益（benefit）Response VO")
@Data
@ExcelIgnoreUnannotated
public class VipPlanBenefitRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "25561")
    @ExcelProperty("主键")
    private Long id;

    @Schema(description = "套餐编码：VIP / SVIP", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("套餐编码")
    private String planCode;

    @Schema(description = "权益 key", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("权益 key")
    private String benefitKey;

    @Schema(description = "权益名称（展示）", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("权益名称")
    private String benefitName;

    @Schema(description = "权益描述（展示）")
    @ExcelProperty("权益描述")
    private String benefitDesc;

    @Schema(description = "权益类型：1=BOOLEAN 2=QUOTA 3=LIMIT 4=RESOURCE", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("权益类型")
    private Integer benefitType;

    @Schema(description = "数值（如 1/3/8；-1=不限）", example = "3")
    @ExcelProperty("数值")
    private Integer benefitValue;

    @Schema(description = "周期：0=NONE 1=DAY 2=WEEK 3=MONTH 4=YEAR 9=LIFETIME", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    @ExcelProperty("周期")
    private Integer periodType;

    @Schema(description = "计次策略：1=COUNT 2=UNIQUE_KEY", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("计次策略")
    private Integer consumePolicy;

    @Schema(description = "排序", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("排序")
    private Integer sort;

    @Schema(description = "展示开关：0 隐藏，1 展示", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("展示开关")
    private Integer displayStatus;

    @Schema(description = "功能开关：0 关闭，1 开启", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("功能开关")
    private Integer enabled;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}

