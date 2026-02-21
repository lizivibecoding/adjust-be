package com.hongguoyan.module.biz.controller.admin.viporder.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "管理后台 - 会员订单退款 Request VO（退最近一笔）")
@Data
public class VipOrderRefundReqVO {

    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "123")
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @Schema(description = "套餐编码：VIP / SVIP", requiredMode = Schema.RequiredMode.REQUIRED, example = "VIP")
    @NotBlank(message = "套餐编码不能为空")
    private String planCode;

    @Schema(description = "退款原因（可选）", example = "后台发起退款")
    private String reason;

}

