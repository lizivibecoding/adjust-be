package com.hongguoyan.module.biz.controller.admin.vipplan.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "管理后台 - 套餐权益展示开关更新 Request VO")
@Data
public class VipPlanBenefitDisplayStatusUpdateReqVO {

    @Schema(description = "套餐编码：VIP / SVIP", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "套餐编码不能为空")
    private String planCode;

    @Schema(description = "权益 key", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "权益 key 不能为空")
    private String benefitKey;

    @Schema(description = "展示开关：0 隐藏，1 展示", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "展示开关不能为空")
    @Min(value = 0, message = "展示开关只能为 0/1")
    @Max(value = 1, message = "展示开关只能为 0/1")
    private Integer displayStatus;

}

