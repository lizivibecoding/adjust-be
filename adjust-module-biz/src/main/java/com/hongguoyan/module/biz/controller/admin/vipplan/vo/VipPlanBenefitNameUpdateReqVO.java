package com.hongguoyan.module.biz.controller.admin.vipplan.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Schema(description = "管理后台 - 套餐权益名称更新 Request VO")
@Data
public class VipPlanBenefitNameUpdateReqVO {

    @Schema(description = "套餐编码：VIP / SVIP", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "套餐编码不能为空")
    private String planCode;

    @Schema(description = "权益 key", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "权益 key 不能为空")
    private String benefitKey;

    @Schema(description = "权益名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "权益名称不能为空")
    private String benefitName;

}

