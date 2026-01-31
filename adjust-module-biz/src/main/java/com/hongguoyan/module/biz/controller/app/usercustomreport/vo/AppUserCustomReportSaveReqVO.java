package com.hongguoyan.module.biz.controller.app.usercustomreport.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "用户 APP - 用户AI调剂定制报告新增/修改 Request VO")
@Data
public class AppUserCustomReportSaveReqVO {

    @Schema(description = "算法/报告版本(如v1.0.3)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "算法/报告版本(如v1.0.3)不能为空")
    private String reportVersion;

    @Schema(description = "生成时使用的档案ID(biz_user_profile.id)", example = "30176")
    private Long sourceProfileId;

    @Schema(description = "生成时使用的意向ID(biz_user_intention.id)", example = "13512")
    private Long sourceIntentionId;

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

