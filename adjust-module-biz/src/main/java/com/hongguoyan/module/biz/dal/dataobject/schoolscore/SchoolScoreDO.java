package com.hongguoyan.module.biz.dal.dataobject.schoolscore;

import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import com.hongguoyan.framework.mybatis.core.dataobject.BaseDO;

/**
 * 自划线 DO
 *
 * @author hgy
 */
@TableName("biz_school_score")
@KeySequence("biz_school_score_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SchoolScoreDO extends BaseDO {

    /**
     * 院校分数线ID
     */
    @TableId
    private Long id;
    /**
     * 学校ID(biz_school.id)
     */
    private Long schoolId;
    /**
     * 学校名称(冗余)
     */
    private String schoolName;
    /**
     * 学院名称(关键区分字段)
     */
    private String collegeName;
    /**
     * 专业ID(biz_major.id)
     */
    private Long majorId;
    /**
     * 专业代码(如010102)
     */
    private String majorCode;
    /**
     * 专业名称
     */
    private String majorName;
    /**
     * 学位类型(0=不区分,1=学硕,2=专硕)
     */
    private Integer degreeType;
    /**
     * 年份(如2025)
     */
    private Short year;
    /**
     * 政治
     */
    private BigDecimal scoreSubject1;
    /**
     * 外语
     */
    private BigDecimal scoreSubject2;
    /**
     * 业务课1
     */
    private BigDecimal scoreSubject3;
    /**
     * 业务课2
     */
    private BigDecimal scoreSubject4;
    /**
     * 总分
     */
    private BigDecimal scoreTotal;
    /**
     * 备注
     */
    private String remark;


}