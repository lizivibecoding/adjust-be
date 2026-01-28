package com.hongguoyan.module.biz.dal.dataobject.vipcouponbatch;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import com.hongguoyan.framework.mybatis.core.dataobject.BaseDO;

/**
 * 会员券码批次 DO
 *
 * @author hgy
 */
@TableName("biz_vip_coupon_batch")
@KeySequence("biz_vip_coupon_batch_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VipCouponBatchDO extends BaseDO {

    /**
     * 主键
     */
    @TableId
    private Long id;
    /**
     * 批次号
     */
    private String batchNo;
    /**
     * 套餐编码
     */
    private String planCode;
    /**
     * 开始时间
     */
    private LocalDateTime validStartTime;
    /**
     * 截止时间
     */
    private LocalDateTime validEndTime;
    /**
     * 总数
     */
    private Integer totalCount;
    /**
     * 已使用数
     */
    private Integer usedCount;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 备注
     */
    private String remark;


}