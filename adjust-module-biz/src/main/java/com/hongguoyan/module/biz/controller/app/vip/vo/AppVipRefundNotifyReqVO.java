package com.hongguoyan.module.biz.controller.app.vip.vo;

import com.fasterxml.jackson.annotation.JsonAlias;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * VIP refund notify request.
 *
 * <p>Minimal fields for idempotent refund callback handling.</p>
 */
@Data
public class AppVipRefundNotifyReqVO {

    @Schema(description = "商户订单号（对应会员订单 orderNo）", requiredMode = Schema.RequiredMode.REQUIRED, example = "2020919026721615872")
    @NotBlank(message = "merchantOrderId 不能为空")
    @JsonAlias({"merchant_order_id"})
    private String merchantOrderId;

    @Schema(description = "支付中台退款单 ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "119")
    @NotNull(message = "payRefundId 不能为空")
    @JsonAlias({"pay_refund_id"})
    private Long payRefundId;

    @Schema(description = "退款金额（分）", requiredMode = Schema.RequiredMode.REQUIRED, example = "9900")
    @NotNull(message = "refundPrice 不能为空")
    @JsonAlias({"refund_price"})
    private Integer refundPrice;

    @Schema(description = "退款成功时间（可选；无法解析时将使用当前时间）", example = "2026-02-10 12:00:00")
    @JsonAlias({"success_time", "refund_time"})
    private String successTime;
}

