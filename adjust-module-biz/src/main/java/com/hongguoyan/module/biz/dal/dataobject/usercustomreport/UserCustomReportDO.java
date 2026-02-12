package com.hongguoyan.module.biz.dal.dataobject.usercustomreport;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hongguoyan.framework.mybatis.core.dataobject.BaseDO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 用户AI调剂定制报告 DO
 *
 * @author hgy
 */
@TableName("biz_user_custom_report")
@KeySequence("biz_user_custom_report_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCustomReportDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 报告版本号(用户内递增，从1开始)
     */
    private Integer reportNo;
    /**
     * 算法/报告版本(如v1.0.3)
     */
    private String reportVersion;
    /**
     * 报告名称（默认：yyyyMMdd - 调剂报告 - nn；用户可修改）
     */
    private String reportName;
    /**
     * 生成时使用的档案ID(biz_user_profile.id)
     */
    private Long sourceProfileId;
    /**
     * 生成时使用的意向ID(biz_user_intention.id)
     */
    private Long sourceIntentionId;
    /**
     * 用户基础信息快照 (JSON)
     */
    private String sourceProfileJson;
    /**
     * 用户意向信息快照 (JSON)
     */
    private String sourceIntentionJson;
    /**
     * 雷达图-背景维度评分(0-100)
     */
    private Integer dimBackgroundScore;
    /**
     * 雷达图-目标院校层次评分(0-100)
     */
    private Integer dimTargetSchoolLevelScore;
    /**
     * 雷达图-软实力评分(0-100)
     */
    private Integer dimSoftSkillsScore;
    /**
     * 雷达图-专业竞争力评分(0-100)
     */
    private Integer dimMajorCompetitivenessScore;
    /**
     * 雷达图-总分维度评分(0-100)
     */
    private Integer dimTotalScore;
    /**
     * 背景维度分析文案
     */
    private String analysisBackground;
    /**
     * 目标院校层次分析文案
     */
    private String analysisTargetSchoolLevel;
    /**
     * 软实力分析文案
     */
    private String analysisSoftSkills;
    /**
     * 专业竞争力分析文案
     */
    private String analysisMajorCompetitiveness;
    /**
     * 总分维度分析文案
     */
    private String analysisTotal;
    /**
     * 报告PDF文件URL
     */
    private String reportPdfUrl;
    /**
     * 生成状态：0-生成中，1-已完成
     */
    private Integer generateStatus;

}

