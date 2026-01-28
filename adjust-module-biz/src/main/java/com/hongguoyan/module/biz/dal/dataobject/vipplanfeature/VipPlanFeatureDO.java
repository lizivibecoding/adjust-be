package com.hongguoyan.module.biz.dal.dataobject.vipplanfeature;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import com.hongguoyan.framework.mybatis.core.dataobject.BaseDO;

/**
 * 会员套餐权益 DO
 *
 * @author hgy
 */
@TableName("biz_vip_plan_feature")
@KeySequence("biz_vip_plan_feature_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VipPlanFeatureDO extends BaseDO {

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
     * 功能点 key
     */
    private String featureKey;
    /**
     * 权益名称
     */
    private String name;
    /**
     * 权益描述
     */
    private String description;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 展示开关：0 隐藏，1 展示
     */
    private Integer status;
    /**
     * 功能开关：0 关闭，1 开启
     */
    private Integer enabled;


}