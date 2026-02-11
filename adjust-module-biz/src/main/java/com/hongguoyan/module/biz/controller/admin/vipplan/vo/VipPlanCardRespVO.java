package com.hongguoyan.module.biz.controller.admin.vipplan.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = "管理后台 - 套餐卡片 Response VO")
@Data
public class VipPlanCardRespVO {

    @Schema(description = "套餐编码：VIP / SVIP", requiredMode = Schema.RequiredMode.REQUIRED)
    private String planCode;

    @Schema(description = "套餐名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String planName;

    @Schema(description = "价格（单位：分）", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer planPrice;

    @Schema(description = "权益列表", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<VipPlanBenefitItemRespVO> benefits;

}
