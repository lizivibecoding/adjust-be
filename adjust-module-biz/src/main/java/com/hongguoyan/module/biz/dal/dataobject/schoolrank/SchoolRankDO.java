package com.hongguoyan.module.biz.dal.dataobject.schoolrank;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hongguoyan.framework.mybatis.core.dataobject.BaseDO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * 软科排名 DO
 *
 * @author hgy
 */
@TableName("biz_school_rank")
@KeySequence("biz_school_rank_seq")
@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SchoolRankDO {

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
     * 学校名称（来源数据）
     */
    @TableField("school_name")
    private String schoolName;

    /**
     * 学校 ID（biz_school.id）
     */
    @TableField("school_id")
    private Long schoolId;

    /**
     * 软科得分（可空）
     */
    private BigDecimal score;
}

