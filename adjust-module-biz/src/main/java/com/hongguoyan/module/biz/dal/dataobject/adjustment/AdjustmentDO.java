package com.hongguoyan.module.biz.dal.dataobject.adjustment;

import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import com.hongguoyan.framework.mybatis.core.dataobject.BaseDO;

/**
 * 调剂 DO
 *
 * @author hgy
 */
@TableName("biz_adjustment")
@KeySequence("biz_adjustment_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdjustmentDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;
    /**
     * 调剂年份
     */
    private Integer year;
    /**
     * 来源类型(1=研招网, 2=院校官网, 3=人工/第三方)
     */
    private Integer sourceType;
    /**
     * 来源URL/原文链接
     */
    private String sourceUrl;
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
     * 专业代码
     */
    private String majorCode;
    /**
     * 专业名称
     */
    private String majorName;
    /**
     * 学位类型(0=未知/不区分,1=学硕,2=专硕)
     */
    private Integer degreeType;
    /**
     * 方向代码
     */
    private String directionCode;
    /**
     * 方向名称
     */
    private String directionName;
    /**
     * 方向ID（biz_school_direction.id）
     */
    private Long directionId;
    /**
     * 学习方式：1-全日制 2-非全日制
     */
    private Integer studyMode;
    /**
     * 调剂缺额人数
     */
    private Integer adjustCount;
    /**
     * 原计划招生人数(参考)
     */
    private Integer adjustLeft;
    /**
     * 调剂缺额人数统计范围（1-专业维度 2-方向维度）
     */
    private Integer adjustCountScope;
    /**
     * 学制(年)
     */
    private BigDecimal studyYears;
    /**
     * 学费说明
     */
    private String tuitionFee;
    /**
     * 复试比例(如: 1:1.2)
     */
    private String retestRatio;
    /**
     * 成绩权重(如: 初试50%+复试50%)
     */
    private String retestWeight;
    /**
     * 复试参考书目
     */
    private String retestBooks;
    /**
     * 分数要求描述(如: 过国家线, 单科>50)
     */
    private String requireScore;
    /**
     * 允许调入的一志愿专业范围
     */
    private String requireMajor;
    /**
     * 考试科目 JSON（原始 JSON 字符串，包含 s1~s4 + subject_codes 冗余字段）
     */
    private String subjects;
    /**
     * 备注
     */
    private String remark;

    /**
     * 招生状态(1=正常招生,0=已经停招)
     */
    private Integer adjustStatus;

    /**
     * 是否专项计划(1=专项计划,0=否)
     */
    private Integer specialPlan;

    /**
     * 调剂类型(1=校内调剂,2=校外调剂)
     */
    private Integer adjustType;
    /**
     * 状态(1=开放, 0=关闭)
     */
    private Integer status;
    /**
     * 发布时间
     */
    private LocalDateTime publishTime;
    /**
     * 浏览次数
     */
    private Integer viewCount;

    /**
     * 热度累计分（点击/订阅等累计）
     */
    private Long hotScore;


}