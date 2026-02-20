package com.hongguoyan.module.biz.controller.admin.recommend.report.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 调剂报告 Response VO")
@Data
public class UserCustomReportRespVO {

    @Schema(description = "报告ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long userId;

    @Schema(description = "用户姓名")
    private String userName;

    @Schema(description = "报告编号 (递增)", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer reportNo;

    @Schema(description = "报告名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "20250208 - 调剂报告 - 01")
    private String reportName;

    @Schema(description = "源画像ID", example = "10")
    private Long sourceProfileId;

    @Schema(description = "源意向ID", example = "20")
    private Long sourceIntentionId;

    @Schema(description = "背景维度分", example = "80")
    private Integer dimBackgroundScore;

    @Schema(description = "综合维度分", example = "85")
    private Integer dimTotalScore;

    @Schema(description = "目标院校层次分", example = "90")
    private Integer dimTargetSchoolLevelScore;

    @Schema(description = "专业竞争力分", example = "75")
    private Integer dimMajorCompetitivenessScore;

    @Schema(description = "软实力维度分", example = "88")
    private Integer dimSoftSkillsScore;

    @Schema(description = "背景维度分析")
    private String analysisBackground;

    @Schema(description = "综合维度分析")
    private String analysisTotal;

    @Schema(description = "目标院校层次分析")
    private String analysisTargetSchoolLevel;

    @Schema(description = "专业竞争力分析")
    private String analysisMajorCompetitiveness;

    @Schema(description = "软实力维度分析")
    private String analysisSoftSkills;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}
