package com.hongguoyan.module.biz.dal.dataobject.adjustmentadmit;

import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import com.hongguoyan.framework.mybatis.core.dataobject.BaseDO;

/**
 * 调剂录取名单 DO
 *
 * @author hgy
 */
@TableName("biz_adjustment_admit")
@KeySequence("biz_adjustment_admit_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdjustmentAdmitDO extends BaseDO {

    /**
     * 调剂录取名单ID
     */
    @TableId
    private Long id;
    /**
     * 学校ID
     */
    private Long schoolId;
    /**
     * 学校名称
     */
    private String schoolName;
    /**
     * 学院ID
     */
    private Long collegeId;
    /**
     * 学院名称
     */
    private String collegeName;
    /**
     * 专业ID
     */
    private Long majorId;
    /**
     * 专业名称
     */
    private String majorName;
    /**
     * 专业代码
     */
    private String majorCode;
    /**
     * 方向ID
     */
    private Long directionId;
    /**
     * 方向名称
     */
    private String directionName;
    /**
     * 年份(如2025)
     */
    private Short year;
    /**
     * 学习方式(全日制/非全日制)
     */
    private String studyMode;
    /**
     * 考生名称(脱敏)
     */
    private String candidateName;
    /**
     * 一志愿学校ID
     */
    private Long firstSchoolId;
    /**
     * 一志愿学校名称
     */
    private String firstSchoolName;
    /**
     * 初试成绩
     */
    private BigDecimal firstScore;
    /**
     * 复试成绩
     */
    private BigDecimal retestScore;
    /**
     * 总成绩
     */
    private BigDecimal totalScore;


}