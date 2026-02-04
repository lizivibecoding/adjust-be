package com.hongguoyan.module.biz.dal.dataobject.vipbenefitlog;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import com.hongguoyan.framework.mybatis.core.dataobject.BaseDO;

/**
 * 用户权益消耗明细 DO
 *
 * @author hgy
 */
@TableName("biz_vip_benefit_log")
@KeySequence("biz_vip_benefit_log_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VipBenefitLogDO extends BaseDO {

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
     * 周期结束时间
     */
    private LocalDateTime periodEndTime;
    /**
     * 本次消耗次数（一般为 1）
     */
    private Integer consumeCount;
    /**
     * 关联类型（如 CUSTOM_REPORT/VOLUNTEER_EXPORT/SUBJECT_CATEGORY_OPEN）
     */
    private String refType;
    /**
     * 关联ID（报告ID/导出ID等）
     */
    private String refId;
    /**
     * 去重键（consume_policy=UNIQUE_KEY 时使用）
     */
    private String uniqueKey;
    /**
     * 备注
     */
    private String remark;


}