package com.hongguoyan.module.biz.controller.app.usercustomreport.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Data;

@Schema(description = "用户 APP - 用户AI调剂定制报告 Response VO")
@Data
@ExcelIgnoreUnannotated
public class AppUserCustomReportRespVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "11001")
    @ExcelProperty("主键ID")
    private Long id;

    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "25247")
    @ExcelProperty("用户ID")
    private Long userId;

    @Schema(description = "报告版本号(用户内递增，从1开始)", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("报告版本号(用户内递增，从1开始)")
    private Integer reportNo;

    @Schema(description = "算法/报告版本(如v1.0.3)", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("算法/报告版本(如v1.0.3)")
    private String reportVersion;

    @Schema(description = "生成时使用的档案ID(biz_user_profile.id)", example = "30176")
    @ExcelProperty("生成时使用的档案ID(biz_user_profile.id)")
    private Long sourceProfileId;

    @Schema(description = "生成时使用的意向ID(biz_user_intention.id)", example = "13512")
    @ExcelProperty("生成时使用的意向ID(biz_user_intention.id)")
    private Long sourceIntentionId;

    @Schema(description = "雷达图-背景维度评分(0-100)", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("雷达图-背景维度评分(0-100)")
    private Integer dimBackgroundScore;

    @Schema(description = "雷达图-地区维度评分(0-100)", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("雷达图-地区维度评分(0-100)")
    private Integer dimLocationScore;

    @Schema(description = "雷达图-英语维度评分(0-100)", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("雷达图-英语维度评分(0-100)")
    private Integer dimEnglishScore;

    @Schema(description = "雷达图-类型维度评分(0-100)", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("雷达图-类型维度评分(0-100)")
    private Integer dimTypeScore;

    @Schema(description = "雷达图-总分维度评分(0-100)", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("雷达图-总分维度评分(0-100)")
    private Integer dimTotalScore;

    @Schema(description = "背景维度分析文案", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("背景维度分析文案")
    private String analysisBackground;

    @Schema(description = "地区维度分析文案", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("地区维度分析文案")
    private String analysisLocation;

    @Schema(description = "英语维度分析文案", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("英语维度分析文案")
    private String analysisEnglish;

    @Schema(description = "类型维度分析文案", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("类型维度分析文案")
    private String analysisType;

    @Schema(description = "总分维度分析文案", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("总分维度分析文案")
    private String analysisTotal;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}

