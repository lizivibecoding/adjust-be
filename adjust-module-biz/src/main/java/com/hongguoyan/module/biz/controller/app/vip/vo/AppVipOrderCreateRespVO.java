package com.hongguoyan.module.biz.controller.app.vip.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "用户 APP - 创建会员订单 Response VO")
@Data
public class AppVipOrderCreateRespVO {

    @Schema(description = "业务订单号", example = "202601280001")
    private String orderNo;

    @Schema(description = "订单金额（单位：分）", example = "9900")
    private Integer amount;

    @Schema(description = "订单状态：1 待支付", example = "1")
    private Integer status;

    @Schema(description = "支付过期时间")
    private LocalDateTime expireTime;

    /**
     * TODO(pay): 后续接入支付后，这里可以返回 pay 拉起参数（如 payOrderId、支付签名参数等）
     */
    @Schema(description = "预留：支付中台订单ID（未接支付时为 null）", example = "900001")
    private Long payOrderId;

}

