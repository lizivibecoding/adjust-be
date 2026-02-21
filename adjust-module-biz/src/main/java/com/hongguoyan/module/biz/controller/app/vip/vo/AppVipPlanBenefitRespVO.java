package com.hongguoyan.module.biz.controller.app.vip.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "套餐权益-响应")
@Data
public class AppVipPlanBenefitRespVO {

    @Schema(description = "权益描述")
    private String benefitDesc;

}

