package com.hongguoyan.module.biz.controller.admin.vipplan.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 套餐卡片权益项 Response VO")
@Data
public class VipPlanBenefitItemRespVO {

    @Schema(description = "权益 key", requiredMode = Schema.RequiredMode.REQUIRED)
    private String benefitKey;

    @Schema(description = "权益名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String benefitName;

    @Schema(description = "权益类型：1=BOOLEAN 2=QUOTA 3=LIMIT", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer benefitType;

    @Schema(description = "权益次数值（-1 表示不限）")
    private Integer benefitValue;

    @Schema(description = "展示开关：0 隐藏，1 展示", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer displayStatus;

}
