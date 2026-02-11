package com.hongguoyan.module.biz.controller.admin.vipplan.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "管理后台 - 套餐卡片更新 Request VO")
@Data
public class VipPlanCardUpdateReqVO {

    @Schema(description = "套餐编码：VIP / SVIP", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "套餐编码不能为空")
    private String planCode;

    @Schema(description = "价格（单位：分）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "价格不能为空")
    @Min(value = 0, message = "价格不能小于 0")
    private Integer planPrice;

    @Schema(description = "开放专业门类次数（-1 表示不限）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "开放专业门类次数不能为空")
    @Min(value = -1, message = "开放专业门类次数不能小于 -1")
    private Integer majorCategoryOpenCount;

    @Schema(description = "AI 定制报告次数（-1 表示不限）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "AI 定制报告次数不能为空")
    @Min(value = -1, message = "AI 定制报告次数不能小于 -1")
    private Integer userReportCount;

}
