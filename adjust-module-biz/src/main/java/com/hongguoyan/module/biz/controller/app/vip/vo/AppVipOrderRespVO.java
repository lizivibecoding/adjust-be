package com.hongguoyan.module.biz.controller.app.vip.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "我的订单-响应")
@Data
public class AppVipOrderRespVO {

    @Schema(description = "订单号", example = "ORD20260101001")
    private String orderNo;

    @Schema(description = "套餐编码：VIP/SVIP", example = "VIP")
    private String planCode;

    @Schema(description = "套餐名称", example = "VIP 月卡")
    private String planName;

    @Schema(description = "订单状态：1待付,2已付,3过期,4退款,5取消", example = "2")
    private Integer status;

    @Schema(description = "展示状态", example = "生效中")
    private String displayStatus;

    @Schema(description = "购买时间")
    private LocalDateTime buyTime;

    @Schema(description = "到期时间")
    private LocalDateTime endTime;

    @Schema(description = "支付过期时间")
    private LocalDateTime expireTime;

}

