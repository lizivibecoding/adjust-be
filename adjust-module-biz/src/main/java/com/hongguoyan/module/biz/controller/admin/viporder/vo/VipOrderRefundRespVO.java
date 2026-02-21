package com.hongguoyan.module.biz.controller.admin.viporder.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 会员订单退款 Response VO（实际退款订单）")
@Data
public class VipOrderRefundRespVO {

    @Schema(description = "订单ID", example = "1001")
    private Long orderId;

    @Schema(description = "订单号（biz 订单号）", example = "1870000000000000001")
    private String orderNo;

    @Schema(description = "支付中台退款单ID", example = "2001")
    private Long payRefundId;

    @Schema(description = "商户退款号", example = "1870000000000000002")
    private String merchantRefundId;

}

