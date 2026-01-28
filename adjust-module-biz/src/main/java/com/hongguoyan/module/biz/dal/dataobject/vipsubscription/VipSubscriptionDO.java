package com.hongguoyan.module.biz.dal.dataobject.vipsubscription;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import com.hongguoyan.framework.mybatis.core.dataobject.BaseDO;

/**
 * 用户会员订阅 DO
 *
 * @author hgy
 */
@TableName("biz_vip_subscription")
@KeySequence("biz_vip_subscription_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VipSubscriptionDO extends BaseDO {

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
     * 套餐编码
     */
    private String planCode;
    /**
     * 首次开通时间
     */
    private LocalDateTime startTime;
    /**
     * 当前到期时间
     */
    private LocalDateTime endTime;
    /**
     * 最近一次续期来源：1 支付，2 券码，3 后台
     */
    private Integer source;


}