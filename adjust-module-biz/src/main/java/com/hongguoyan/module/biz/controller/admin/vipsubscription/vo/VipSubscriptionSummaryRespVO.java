package com.hongguoyan.module.biz.controller.admin.vipsubscription.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 用户会员订阅统计 Response VO")
@Data
public class VipSubscriptionSummaryRespVO {

    @Schema(description = "VIP 会员数量", example = "1234")
    private Long vipCount;

    @Schema(description = "SVIP 会员数量", example = "456")
    private Long svipCount;

    @Schema(description = "本月新增数量", example = "89")
    private Long monthNewCount;
}
