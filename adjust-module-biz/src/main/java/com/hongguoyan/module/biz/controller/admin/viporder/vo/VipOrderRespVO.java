package com.hongguoyan.module.biz.controller.admin.viporder.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import cn.idev.excel.annotation.*;

@Schema(description = "管理后台 - 会员订单 Response VO")
@Data
@ExcelIgnoreUnannotated
public class VipOrderRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "14235")
    @ExcelProperty("主键")
    private Long id;

    @Schema(description = "订单号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("订单号")
    private String orderNo;

    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "8127")
    @ExcelProperty("用户ID")
    private Long userId;

    @Schema(description = "套餐编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("套餐编码")
    private String planCode;

    @Schema(description = "金额（分）", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("金额（分）")
    private Integer amount;

    @Schema(description = "状态：1待付,2已付,3过期,4退款,5取消", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("状态：1待付,2已付,3过期,4退款,5取消")
    private Integer status;

    @Schema(description = "支付中台订单ID", example = "9115")
    @ExcelProperty("支付中台订单ID")
    private Long payOrderId;

    @Schema(description = "支付渠道")
    @ExcelProperty("支付渠道")
    private String payChannel;

    @Schema(description = "支付时间")
    @ExcelProperty("支付时间")
    private LocalDateTime payTime;

    @Schema(description = "订单过期时间")
    @ExcelProperty("订单过期时间")
    private LocalDateTime expireTime;

    @Schema(description = "退款金额（分）")
    @ExcelProperty("退款金额（分）")
    private Integer refundAmount;

    @Schema(description = "退款时间")
    @ExcelProperty("退款时间")
    private LocalDateTime refundTime;

    @Schema(description = "支付中台退款单ID", example = "4280")
    @ExcelProperty("支付中台退款单ID")
    private Long payRefundId;

    @Schema(description = "取消时间")
    @ExcelProperty("取消时间")
    private LocalDateTime cancelTime;

    @Schema(description = "扩展字段")
    @ExcelProperty("扩展字段")
    private String extra;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}