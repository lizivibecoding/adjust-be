package com.hongguoyan.module.biz.dal.dataobject.adjustment;

import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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
     * 学习方式(全日制/非全日制)
     */
    private String studyMode;
    /**
     * 调剂缺额人数
     */
    private Integer adjustCount;
    /**
     * 原计划招生人数(参考)
     */
    private Integer adjustLeft;
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
     * 科目1代码
     */
    private String subjectCode1;
    /**
     * 科目1名称
     */
    private String subjectName1;
    /**
     * 科目1说明
     */
    private String subjectNote1;
    /**
     * 科目2代码
     */
    private String subjectCode2;
    /**
     * 科目2名称
     */
    private String subjectName2;
    /**
     * 科目2说明
     */
    private String subjectNote2;
    /**
     * 科目3代码
     */
    private String subjectCode3;
    /**
     * 科目3名称
     */
    private String subjectName3;
    /**
     * 科目3说明
     */
    private String subjectNote3;
    /**
     * 科目4代码
     */
    private String subjectCode4;
    /**
     * 科目4名称
     */
    private String subjectName4;
    /**
     * 科目4说明
     */
    private String subjectNote4;
    /**
     * 科目组合JSON
     */
    private String subjectCombinations;
    /**
     * 发布人用户ID(0代表系统爬虫)
     */
    private Long userId;
    /**
     * 联系方式
     */
    private String contact;
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
     * 审核状态(0=待审, 1=通过, 2=拒绝)
     */
    private Integer reviewStatus;
    /**
     * 审核人ID
     */
    private Long reviewer;
    /**
     * 发布时间
     */
    private LocalDateTime publishTime;
    /**
     * 浏览次数
     */
    private Integer viewCount;


}