package com.hongguoyan.module.biz.controller.admin.viporder.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.hongguoyan.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static com.hongguoyan.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 会员订单分页 Request VO")
@Data
public class VipOrderPageReqVO extends PageParam {

    @Schema(description = "订单号")
    private String orderNo;

    @Schema(description = "用户ID", example = "8127")
    private Long userId;

    @Schema(description = "套餐编码")
    private String planCode;

    @Schema(description = "金额（分）")
    private Integer amount;

    @Schema(description = "状态：1待付,2已付,3过期,4退款,5取消", example = "1")
    private Integer status;

    @Schema(description = "支付中台订单ID", example = "9115")
    private Long payOrderId;

    @Schema(description = "支付渠道")
    private String payChannel;

    @Schema(description = "支付时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] payTime;

    @Schema(description = "订单过期时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] expireTime;

    @Schema(description = "退款金额（分）")
    private Integer refundAmount;

    @Schema(description = "退款时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] refundTime;

    @Schema(description = "支付中台退款单ID", example = "4280")
    private Long payRefundId;

    @Schema(description = "取消时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] cancelTime;

    @Schema(description = "扩展字段")
    private String extra;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}