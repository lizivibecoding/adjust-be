package com.hongguoyan.module.biz.dal.dataobject.schoolrank;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hongguoyan.framework.mybatis.core.dataobject.BaseDO;
import lombok.*;

import java.math.BigDecimal;

/**
 * 软科排名 DO
 *
 * @author hgy
 */
@TableName("biz_school_rank")
@KeySequence("biz_school_rank_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SchoolRankDO extends BaseDO {

    /**
     * 软科排名ID
     */
    @TableId
    private Long id;

    /**
     * 排名年份
     */
    private Integer year;

    /**
     * 软科排名（越小越好）
     */
    private Integer ranking;

    /**
     * 学校名称
     */
    @TableField("school_name")
    private String schoolName;

    /**
     * 学校ID（biz_school.id）
     */
    @TableField("school_id")
    private Long schoolId;

    /**
     * 软科得分
     */
    private BigDecimal score;
}

