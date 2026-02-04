package com.hongguoyan.module.biz.dal.dataobject.vipplan;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import com.hongguoyan.framework.mybatis.core.dataobject.BaseDO;

/**
 * 会员套餐 DO
 *
 * @author hgy
 */
@TableName("biz_vip_plan")
@KeySequence("biz_vip_plan_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VipPlanDO extends BaseDO {

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
     * 套餐名称
     */
    private String planName;
    /**
     * 价格（单位：分）
     */
    private Integer planPrice;
    /**
     * 增加时长（单位：天）
     */
    private Integer durationDays;
    /**
     * 状态：0 禁用，1 启用
     */
    private Integer status;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 备注
     */
    private String remark;


}