package com.hongguoyan.module.biz.dal.dataobject.candidatecustomreports;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import com.hongguoyan.framework.mybatis.core.dataobject.BaseDO;

/**
 * 考生AI调剂定制报告 DO
 *
 * @author hgy
 */
@TableName("biz_candidate_custom_reports")
@KeySequence("biz_candidate_custom_reports_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CandidateCustomReportsDO extends BaseDO {

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
     * 生成时使用的档案ID(biz_candidate_profiles.id)
     */
    private Long sourceProfileId;
    /**
     * 生成时使用的偏好ID(biz_candidate_preferences.id)
     */
    private Long sourcePreferencesId;
    /**
     * 雷达图-背景维度评分(0-100)
     */
    private Integer dimBackgroundScore;
    /**
     * 雷达图-地区维度评分(0-100)
     */
    private Integer dimLocationScore;
    /**
     * 雷达图-英语维度评分(0-100)
     */
    private Integer dimEnglishScore;
    /**
     * 雷达图-类型维度评分(0-100)
     */
    private Integer dimTypeScore;
    /**
     * 雷达图-总分维度评分(0-100)
     */
    private Integer dimTotalScore;
    /**
     * 背景维度分析文案
     */
    private String analysisBackground;
    /**
     * 地区维度分析文案
     */
    private String analysisLocation;
    /**
     * 英语维度分析文案
     */
    private String analysisEnglish;
    /**
     * 类型维度分析文案
     */
    private String analysisType;
    /**
     * 总分维度分析文案
     */
    private String analysisTotal;


}