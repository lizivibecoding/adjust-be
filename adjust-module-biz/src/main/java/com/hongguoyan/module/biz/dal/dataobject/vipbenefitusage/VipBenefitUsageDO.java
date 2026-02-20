package com.hongguoyan.module.biz.dal.dataobject.vipbenefitusage;

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
 * 用户权益用量汇总 DO
 *
 * @author 芋道源码
 */
@TableName("biz_vip_benefit_usage")
@KeySequence("biz_vip_benefit_usage_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VipBenefitUsageDO extends BaseDO {

    /**
     * 主键
     */
    @TableId
    private Long id;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 权益 key
     */
    private String benefitKey;
    /**
     * 周期开始时间（按 period_type 对齐）
     */
    private LocalDateTime periodStartTime;
    /**
     * 周期结束时间（建议为开区间终点）
     */
    private LocalDateTime periodEndTime;
    /**
     * 周期内已用次数
     */
    private Integer usedCount;
    /**
     * 周期内累计发放次数（-1 不限次）
     */
    private Integer grantTotal;
    /**
     * 最近一次消耗时间
     */
    private LocalDateTime lastUsedTime;


}