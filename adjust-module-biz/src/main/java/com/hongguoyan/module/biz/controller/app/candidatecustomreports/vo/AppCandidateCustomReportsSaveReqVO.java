package com.hongguoyan.module.biz.controller.app.candidatecustomreports.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import jakarta.validation.constraints.*;

@Schema(description = "用户 APP - 考生AI调剂定制报告新增/修改 Request VO")
@Data
public class AppCandidateCustomReportsSaveReqVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "11001")
    private Long id;

    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "25247")
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @Schema(description = "报告版本号(用户内递增，从1开始)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "报告版本号(用户内递增，从1开始)不能为空")
    private Integer reportNo;

    @Schema(description = "算法/报告版本(如v1.0.3)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "算法/报告版本(如v1.0.3)不能为空")
    private String reportVersion;

    @Schema(description = "生成时使用的档案ID(biz_candidate_profiles.id)", example = "30176")
    private Long sourceProfileId;

    @Schema(description = "生成时使用的偏好ID(biz_candidate_preferences.id)", example = "13512")
    private Long sourcePreferencesId;

    @Schema(description = "雷达图-背景维度评分(0-100)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "雷达图-背景维度评分(0-100)不能为空")
    private Integer dimBackgroundScore;

    @Schema(description = "雷达图-地区维度评分(0-100)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "雷达图-地区维度评分(0-100)不能为空")
    private Integer dimLocationScore;

    @Schema(description = "雷达图-英语维度评分(0-100)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "雷达图-英语维度评分(0-100)不能为空")
    private Integer dimEnglishScore;

    @Schema(description = "雷达图-类型维度评分(0-100)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "雷达图-类型维度评分(0-100)不能为空")
    private Integer dimTypeScore;

    @Schema(description = "雷达图-总分维度评分(0-100)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "雷达图-总分维度评分(0-100)不能为空")
    private Integer dimTotalScore;

    @Schema(description = "背景维度分析文案", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "背景维度分析文案不能为空")
    private String analysisBackground;

    @Schema(description = "地区维度分析文案", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "地区维度分析文案不能为空")
    private String analysisLocation;

    @Schema(description = "英语维度分析文案", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "英语维度分析文案不能为空")
    private String analysisEnglish;

    @Schema(description = "类型维度分析文案", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotEmpty(message = "类型维度分析文案不能为空")
    private String analysisType;

    @Schema(description = "总分维度分析文案", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "总分维度分析文案不能为空")
    private String analysisTotal;

}