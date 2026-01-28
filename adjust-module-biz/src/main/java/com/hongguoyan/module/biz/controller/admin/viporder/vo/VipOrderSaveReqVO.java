package com.hongguoyan.module.biz.controller.admin.viporder.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 会员订单新增/修改 Request VO")
@Data
public class VipOrderSaveReqVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "14235")
    private Long id;

    @Schema(description = "订单号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "订单号不能为空")
    private String orderNo;

    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "8127")
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @Schema(description = "套餐编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "套餐编码不能为空")
    private String planCode;

    @Schema(description = "金额（分）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "金额（分）不能为空")
    private Integer amount;

    @Schema(description = "状态：1待付,2已付,3过期,4退款,5取消", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "状态：1待付,2已付,3过期,4退款,5取消不能为空")
    private Integer status;

    @Schema(description = "支付中台订单ID", example = "9115")
    private Long payOrderId;

    @Schema(description = "支付渠道")
    private String payChannel;

    @Schema(description = "支付时间")
    private LocalDateTime payTime;

    @Schema(description = "订单过期时间")
    private LocalDateTime expireTime;

    @Schema(description = "退款金额（分）")
    private Integer refundAmount;

    @Schema(description = "退款时间")
    private LocalDateTime refundTime;

    @Schema(description = "支付中台退款单ID", example = "4280")
    private Long payRefundId;

    @Schema(description = "取消时间")
    private LocalDateTime cancelTime;

    @Schema(description = "扩展字段")
    private String extra;

}