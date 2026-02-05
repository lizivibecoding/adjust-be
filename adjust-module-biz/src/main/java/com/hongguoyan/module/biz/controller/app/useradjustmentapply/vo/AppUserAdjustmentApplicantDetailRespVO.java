package com.hongguoyan.module.biz.controller.app.useradjustmentapply.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Schema(description = "申请人详情-响应")
@Data
public class AppUserAdjustmentApplicantDetailRespVO {

    @Schema(description = "申请ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Schema(description = "用户发布调剂ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long userAdjustmentId;

    @Schema(description = "姓名")
    private String candidateName;

    @Schema(description = "联系方式")
    private String contact;

    @Schema(description = "一志愿报考学校名称")
    private String firstSchoolName;

    @Schema(description = "一志愿报考专业名称")
    private String firstMajorName;

    @Schema(description = "第一门成绩")
    private BigDecimal subjectScore1;

    @Schema(description = "第二门成绩")
    private BigDecimal subjectScore2;

    @Schema(description = "第三门成绩")
    private BigDecimal subjectScore3;

    @Schema(description = "第四门成绩")
    private BigDecimal subjectScore4;

    @Schema(description = "总分")
    private BigDecimal totalScore;

    @Schema(description = "自我介绍&个人优势")
    private String note;
}

