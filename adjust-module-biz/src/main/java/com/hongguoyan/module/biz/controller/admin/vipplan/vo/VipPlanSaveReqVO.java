package com.hongguoyan.module.biz.controller.admin.vipplan.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import jakarta.validation.constraints.*;

@Schema(description = "管理后台 - 会员套餐新增/修改 Request VO")
@Data
public class VipPlanSaveReqVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "3933")
    private Long id;

    @Schema(description = "套餐编码：VIP / SVIP", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "套餐编码：VIP / SVIP不能为空")
    private String planCode;

    @Schema(description = "套餐名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "王五")
    @NotEmpty(message = "套餐名称不能为空")
    private String planName;

    @Schema(description = "价格（单位：分）", requiredMode = Schema.RequiredMode.REQUIRED, example = "20441")
    @NotNull(message = "价格（单位：分）不能为空")
    private Integer planPrice;

    @Schema(description = "增加时长（单位：天）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "增加时长（单位：天）不能为空")
    private Integer durationDays;

    @Schema(description = "状态：0 禁用，1 启用", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "状态：0 禁用，1 启用不能为空")
    private Integer status;

    @Schema(description = "排序", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "排序不能为空")
    private Integer sort;

    @Schema(description = "备注", example = "你说的对")
    private String remark;

}