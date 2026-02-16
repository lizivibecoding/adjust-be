package com.hongguoyan.module.biz.controller.admin.vipcoupon.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 会员券码统计 Response VO")
@Data
public class VipCouponSummaryRespVO {

    @Schema(description = "总券码数")
    private Long totalCount;

    @Schema(description = "未使用")
    private Long unusedCount;

    @Schema(description = "已使用")
    private Long usedCount;

    @Schema(description = "已过期")
    private Long expiredCount;

}

