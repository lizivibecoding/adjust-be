package com.hongguoyan.module.biz.dal.dataobject.vipcouponcode;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import com.hongguoyan.framework.mybatis.core.dataobject.BaseDO;

/**
 * 会员券码 DO
 *
 * @author hgy
 */
@TableName("biz_vip_coupon_code")
@KeySequence("biz_vip_coupon_code_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VipCouponCodeDO extends BaseDO {

    /**
     * 主键
     */
    @TableId
    private Long id;
    /**
     * 券码
     */
    private String code;
    /**
     * 批次号
     */
    private String batchNo;
    /**
     * 套餐编码
     */
    private String planCode;
    /**
     * 状态：1未使用,2已使用,3过期,4作废
     */
    private Integer status;
    /**
     * 开始时间
     */
    private LocalDateTime validStartTime;
    /**
     * 截止时间
     */
    private LocalDateTime validEndTime;
    /**
     * 使用人ID
     */
    private Long userId;
    /**
     * 使用时间
     */
    private LocalDateTime usedTime;
    /**
     * 备注
     */
    private String remark;


}