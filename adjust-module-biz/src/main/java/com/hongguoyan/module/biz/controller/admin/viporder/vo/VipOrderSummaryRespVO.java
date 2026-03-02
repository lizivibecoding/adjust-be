package com.hongguoyan.module.biz.controller.admin.viporder.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 会员订单统计 Response VO")
@Data
public class VipOrderSummaryRespVO {

    @Schema(description = "VIP 会员数量", example = "123")
    private Long vipCount;

    @Schema(description = "SVIP 会员数量", example = "45")
    private Long svipCount;

    @Schema(description = "会员数量", example = "168")
    private Long memberCount;

    @Schema(description = "收入总额（分）", example = "10000")
    private Long incomeAmount;

    @Schema(description = "退款总额（分）", example = "2000")
    private Long refundAmount;

}

