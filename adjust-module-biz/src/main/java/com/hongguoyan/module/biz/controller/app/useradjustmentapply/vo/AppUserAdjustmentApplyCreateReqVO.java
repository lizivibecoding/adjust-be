package com.hongguoyan.module.biz.controller.app.useradjustmentapply.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Schema(description = "用户 APP - 用户发布调剂申请创建 Request VO（apply-form）")
@Data
public class AppUserAdjustmentApplyCreateReqVO {

    @Schema(description = "用户发布调剂ID(biz_user_adjustment.id)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2789")
    @NotNull(message = "userAdjustmentId不能为空")
    private Long userAdjustmentId;

    @Schema(description = "姓名", requiredMode = Schema.RequiredMode.REQUIRED, example = "张同学")
    @NotEmpty(message = "姓名不能为空")
    private String candidateName;

    @Schema(description = "联系方式(手机号/微信)", requiredMode = Schema.RequiredMode.REQUIRED, example = "18800001234")
    @NotEmpty(message = "联系方式不能为空")
    private String contact;

    @Schema(description = "一志愿报考学校ID(biz_school.id)", requiredMode = Schema.RequiredMode.REQUIRED, example = "15421")
    @NotNull(message = "firstSchoolId不能为空")
    private Long firstSchoolId;

    @Schema(description = "一志愿报考专业ID(biz_major.id)", requiredMode = Schema.RequiredMode.REQUIRED, example = "13734")
    @NotNull(message = "firstMajorId不能为空")
    private Long firstMajorId;

    @Schema(description = "第一门成绩", requiredMode = Schema.RequiredMode.REQUIRED, example = "70")
    @NotNull(message = "第一门成绩不能为空")
    private BigDecimal subjectScore1;

    @Schema(description = "第二门成绩", requiredMode = Schema.RequiredMode.REQUIRED, example = "68")
    @NotNull(message = "第二门成绩不能为空")
    private BigDecimal subjectScore2;

    @Schema(description = "第三门成绩", requiredMode = Schema.RequiredMode.REQUIRED, example = "120")
    @NotNull(message = "第三门成绩不能为空")
    private BigDecimal subjectScore3;

    @Schema(description = "第四门成绩", requiredMode = Schema.RequiredMode.REQUIRED, example = "116")
    @NotNull(message = "第四门成绩不能为空")
    private BigDecimal subjectScore4;

    @Schema(description = "总分(可不传；不传则后端计算)", example = "374")
    private BigDecimal totalScore;

    @Schema(description = "自我介绍&个人优势", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "自我介绍&个人优势不能为空")
    private String note;
}

