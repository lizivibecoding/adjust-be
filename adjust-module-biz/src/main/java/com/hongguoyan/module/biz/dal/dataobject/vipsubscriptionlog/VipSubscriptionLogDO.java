package com.hongguoyan.module.biz.dal.dataobject.vipsubscriptionlog;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hongguoyan.framework.mybatis.core.dataobject.BaseDO;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 会员订阅变更流水 DO
 *
 * @author hgy
 */
@TableName("biz_vip_subscription_log")
@KeySequence("biz_vip_subscription_log_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VipSubscriptionLogDO extends BaseDO {

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
     * 动作：1开通,2续期,3撤销,4补偿
     */
    private Integer action;
    /**
     * 来源：1支付,2券码,3后台
     */
    private Integer source;
    /**
     * 关联类型
     */
    private Integer refType;
    /**
     * 关联ID
     */
    private String refId;
    /**
     * 变更前到期时间
     */
    private LocalDateTime beforeEndTime;
    /**
     * 变更后到期时间
     */
    private LocalDateTime afterEndTime;
    /**
     * 增加天数
     */
    private Integer grantDays;
    /**
     * 备注
     */
    private String remark;

}

