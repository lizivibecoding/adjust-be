package com.hongguoyan.module.biz.controller.app.vip.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Schema(description = "用户 APP - 券码兑换 Request VO")
@Data
public class AppVipCouponRedeemReqVO {

    @Schema(description = "券码", requiredMode = Schema.RequiredMode.REQUIRED, example = "VIP20260116001")
    @NotBlank(message = "code 不能为空")
    private String code;

}

