package com.hongguoyan.module.biz.controller.admin.vipplan.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import cn.idev.excel.annotation.*;

@Schema(description = "管理后台 - 会员套餐 Response VO")
@Data
@ExcelIgnoreUnannotated
public class VipPlanRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "3933")
    @ExcelProperty("主键")
    private Long id;

    @Schema(description = "套餐编码：VIP / SVIP", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("套餐编码：VIP / SVIP")
    private String planCode;

    @Schema(description = "套餐名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "王五")
    @ExcelProperty("套餐名称")
    private String planName;

    @Schema(description = "价格（单位：分）", requiredMode = Schema.RequiredMode.REQUIRED, example = "20441")
    @ExcelProperty("价格（单位：分）")
    private Integer planPrice;

    @Schema(description = "增加时长（单位：天）", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("增加时长（单位：天）")
    private Integer durationDays;

    @Schema(description = "状态：0 禁用，1 启用", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("状态：0 禁用，1 启用")
    private Integer status;

    @Schema(description = "排序", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("排序")
    private Integer sort;

    @Schema(description = "备注", example = "你说的对")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}