package com.hongguoyan.module.biz.dal.dataobject.majorrank;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * 学校学科排名（软科学科排名） DO
 *
 * 对应表：biz_major_rank
 *
 * @author hgy
 */
@TableName("biz_major_rank")
@KeySequence("biz_major_rank_seq")
@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SchoolMajorRankDO {

    @TableId
    private Long id;

    /**
     * 排名年份
     */
    private Integer year;

    /**
     * 专业代码（来源数据）
     */
    @TableField("major_code")
    private String majorCode;

    /**
     * 专业名称（来源数据）
     */
    @TableField("major_name")
    private String majorName;

    /**
     * 专业 ID（biz_major.id，通过 major_code 回填）
     */
    @TableField("major_id")
    private Long majorId;

    /**
     * 学校名称（来源数据）
     */
    @TableField("school_name")
    private String schoolName;

    /**
     * 学校 ID（biz_school.id，通过 school_name 回填）
     */
    @TableField("school_id")
    private Long schoolId;

    /**
     * 软科得分（可空）
     */
    private BigDecimal score;

    /**
     * 排名（越小越好）
     */
    private Integer ranking;

    /**
     * 等级（可空，数值化）
     */
    private Integer level;

    /**
     * 等级（可空，原始文本）
     */
    @TableField("level_raw")
    private String levelRaw;

    /**
     * 获取计算后的等级（A+/A/A-/B+/B/B-）
     */
    public String getCalculatedLevel() {
        if (level == null) {
            return null;
        }
        if (level <= 3) {
            return "A+";
        } else if (level <= 7) {
            return "A";
        } else if (level <= 12) {
            return "A-";
        } else if (level <= 20) {
            return "B+";
        } else if (level <= 30) {
            return "B";
        } else if (level <= 40) {
            return "B-";
        }
        return null;
    }
}

