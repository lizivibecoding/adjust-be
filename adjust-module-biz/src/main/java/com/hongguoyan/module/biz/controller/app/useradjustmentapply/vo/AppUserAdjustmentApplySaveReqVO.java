package com.hongguoyan.module.biz.controller.app.useradjustmentapply.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Schema(description = "用户 APP - 用户发布调剂申请记录新增/修改 Request VO")
@Data
public class AppUserAdjustmentApplySaveReqVO {

    @Schema(description = "用户发布调剂申请ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "22042")
    private Long id;

    @Schema(description = "用户发布调剂ID(biz_user_adjustment.id)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2789")
    @NotNull(message = "用户发布调剂ID(biz_user_adjustment.id)不能为空")
    private Long userAdjustmentId;

    @Schema(description = "姓名", requiredMode = Schema.RequiredMode.REQUIRED, example = "王五")
    @NotEmpty(message = "姓名不能为空")
    private String candidateName;

    @Schema(description = "联系方式(手机号/微信)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "联系方式(手机号/微信)不能为空")
    private String contact;

    @Schema(description = "一志愿报考学校ID(biz_school.id)", requiredMode = Schema.RequiredMode.REQUIRED, example = "15421")
    @NotNull(message = "一志愿报考学校ID(biz_school.id)不能为空")
    private Long firstSchoolId;

    @Schema(description = "一志愿报考学校名称(冗余)", requiredMode = Schema.RequiredMode.REQUIRED, example = "芋艿")
    @NotEmpty(message = "一志愿报考学校名称(冗余)不能为空")
    private String firstSchoolName;

    @Schema(description = "一志愿报考专业ID(biz_major.id)", requiredMode = Schema.RequiredMode.REQUIRED, example = "13734")
    @NotNull(message = "一志愿报考专业ID(biz_major.id)不能为空")
    private Long firstMajorId;

    @Schema(description = "一志愿报考专业代码(冗余)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "一志愿报考专业代码(冗余)不能为空")
    private String firstMajorCode;

    @Schema(description = "一志愿报考专业名称(冗余)", requiredMode = Schema.RequiredMode.REQUIRED, example = "芋艿")
    @NotEmpty(message = "一志愿报考专业名称(冗余)不能为空")
    private String firstMajorName;

    @Schema(description = "第一门成绩", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "第一门成绩不能为空")
    private BigDecimal subjectScore1;

    @Schema(description = "第二门成绩", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "第二门成绩不能为空")
    private BigDecimal subjectScore2;

    @Schema(description = "第三门成绩", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "第三门成绩不能为空")
    private BigDecimal subjectScore3;

    @Schema(description = "第四门成绩", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "第四门成绩不能为空")
    private BigDecimal subjectScore4;

    @Schema(description = "总分", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "总分不能为空")
    private BigDecimal totalScore;

    @Schema(description = "自我介绍&个人优势", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "自我介绍&个人优势不能为空")
    private String note;

}