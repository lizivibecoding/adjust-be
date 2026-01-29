package com.hongguoyan.module.biz.controller.app.candidatecustomreports.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.hongguoyan.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static com.hongguoyan.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "用户 APP - 考生AI调剂定制报告分页 Request VO")
@Data
public class AppCandidateCustomReportsPageReqVO extends PageParam {

    @Schema(description = "用户ID", example = "25247")
    private Long userId;

    @Schema(description = "报告版本号(用户内递增，从1开始)")
    private Integer reportNo;

    @Schema(description = "算法/报告版本(如v1.0.3)")
    private String reportVersion;

    @Schema(description = "生成时使用的档案ID(biz_candidate_profiles.id)", example = "30176")
    private Long sourceProfileId;

    @Schema(description = "生成时使用的偏好ID(biz_candidate_preferences.id)", example = "13512")
    private Long sourcePreferencesId;

    @Schema(description = "雷达图-背景维度评分(0-100)")
    private Integer dimBackgroundScore;

    @Schema(description = "雷达图-地区维度评分(0-100)")
    private Integer dimLocationScore;

    @Schema(description = "雷达图-英语维度评分(0-100)")
    private Integer dimEnglishScore;

    @Schema(description = "雷达图-类型维度评分(0-100)")
    private Integer dimTypeScore;

    @Schema(description = "雷达图-总分维度评分(0-100)")
    private Integer dimTotalScore;

    @Schema(description = "背景维度分析文案")
    private String analysisBackground;

    @Schema(description = "地区维度分析文案")
    private String analysisLocation;

    @Schema(description = "英语维度分析文案")
    private String analysisEnglish;

    @Schema(description = "类型维度分析文案", example = "1")
    private String analysisType;

    @Schema(description = "总分维度分析文案")
    private String analysisTotal;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}