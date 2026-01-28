package com.hongguoyan.module.biz.dal.dataobject.viporder;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import com.hongguoyan.framework.mybatis.core.dataobject.BaseDO;

/**
 * 会员订单 DO
 *
 * @author hgy
 */
@TableName("biz_vip_order")
@KeySequence("biz_vip_order_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VipOrderDO extends BaseDO {

    /**
     * 主键
     */
    @TableId
    private Long id;
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 套餐编码
     */
    private String planCode;
    /**
     * 金额（分）
     */
    private Integer amount;
    /**
     * 状态：1待付,2已付,3过期,4退款,5取消
     */
    private Integer status;
    /**
     * 支付中台订单ID
     */
    private Long payOrderId;
    /**
     * 支付渠道
     */
    private String payChannel;
    /**
     * 支付时间
     */
    private LocalDateTime payTime;
    /**
     * 订单过期时间
     */
    private LocalDateTime expireTime;
    /**
     * 退款金额（分）
     */
    private Integer refundAmount;
    /**
     * 退款时间
     */
    private LocalDateTime refundTime;
    /**
     * 支付中台退款单ID
     */
    private Long payRefundId;
    /**
     * 取消时间
     */
    private LocalDateTime cancelTime;
    /**
     * 扩展字段
     */
    private String extra;


}