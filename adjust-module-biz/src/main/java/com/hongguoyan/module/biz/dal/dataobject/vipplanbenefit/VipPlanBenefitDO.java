package com.hongguoyan.module.biz.dal.dataobject.vipplanbenefit;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hongguoyan.framework.mybatis.core.dataobject.BaseDO;
import lombok.*;

/**
 * 套餐权益（benefit）DO
 *
 * @author hgy
 */
@TableName("biz_vip_plan_benefit")
@KeySequence("biz_vip_plan_benefit_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VipPlanBenefitDO extends BaseDO {

    /**
     * 主键
     */
    @TableId
    private Long id;
    /**
     * 套餐编码：VIP / SVIP
     */
    private String planCode;
    /**
     * 权益 key
     */
    private String benefitKey;
    /**
     * 权益类型：1=BOOLEAN 2=QUOTA(次数) 3=LIMIT(阈值) 4=RESOURCE(资源类)
     */
    private Integer benefitType;
    /**
     * 数值（如 1/3/8；-1=不限）
     */
    private Integer benefitValue;
    /**
     * 周期：0=NONE 1=DAY 2=WEEK 3=MONTH 4=YEAR 9=LIFETIME
     */
    private Integer periodType;
    /**
     * 计次策略：1=COUNT(每次计) 2=UNIQUE_KEY(按唯一键去重)
     */
    private Integer consumePolicy;
    /**
     * 权益名称（展示）
     */
    private String benefitName;
    /**
     * 权益描述（展示）
     */
    private String benefitDesc;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 展示开关：0 隐藏，1 展示
     */
    private Integer displayStatus;
    /**
     * 功能开关：0 关闭，1 开启
     */
    private Integer enabled;

}

