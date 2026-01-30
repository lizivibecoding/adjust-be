package com.hongguoyan.module.biz.controller.app.useradjustmentapply.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import cn.idev.excel.annotation.*;

@Schema(description = "用户 APP - 用户发布调剂申请记录 Response VO")
@Data
@ExcelIgnoreUnannotated
public class AppUserAdjustmentApplyRespVO {

    @Schema(description = "用户发布调剂申请ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "22042")
    @ExcelProperty("用户发布调剂申请ID")
    private Long id;

    @Schema(description = "用户发布调剂ID(biz_user_adjustment.id)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2789")
    @ExcelProperty("用户发布调剂ID(biz_user_adjustment.id)")
    private Long userAdjustmentId;

    @Schema(description = "申请人用户ID(member.user.id)", requiredMode = Schema.RequiredMode.REQUIRED, example = "1968")
    @ExcelProperty("申请人用户ID(member.user.id)")
    private Long userId;

    @Schema(description = "姓名", requiredMode = Schema.RequiredMode.REQUIRED, example = "王五")
    @ExcelProperty("姓名")
    private String candidateName;

    @Schema(description = "联系方式(手机号/微信)", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("联系方式(手机号/微信)")
    private String contact;

    @Schema(description = "一志愿报考学校ID(biz_school.id)", requiredMode = Schema.RequiredMode.REQUIRED, example = "15421")
    @ExcelProperty("一志愿报考学校ID(biz_school.id)")
    private Long firstSchoolId;

    @Schema(description = "一志愿报考学校名称(冗余)", requiredMode = Schema.RequiredMode.REQUIRED, example = "芋艿")
    @ExcelProperty("一志愿报考学校名称(冗余)")
    private String firstSchoolName;

    @Schema(description = "一志愿报考专业ID(biz_major.id)", requiredMode = Schema.RequiredMode.REQUIRED, example = "13734")
    @ExcelProperty("一志愿报考专业ID(biz_major.id)")
    private Long firstMajorId;

    @Schema(description = "一志愿报考专业代码(冗余)", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("一志愿报考专业代码(冗余)")
    private String firstMajorCode;

    @Schema(description = "一志愿报考专业名称(冗余)", requiredMode = Schema.RequiredMode.REQUIRED, example = "芋艿")
    @ExcelProperty("一志愿报考专业名称(冗余)")
    private String firstMajorName;

    @Schema(description = "第一门成绩", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("第一门成绩")
    private BigDecimal subjectScore1;

    @Schema(description = "第二门成绩", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("第二门成绩")
    private BigDecimal subjectScore2;

    @Schema(description = "第三门成绩", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("第三门成绩")
    private BigDecimal subjectScore3;

    @Schema(description = "第四门成绩", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("第四门成绩")
    private BigDecimal subjectScore4;

    @Schema(description = "总分", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("总分")
    private BigDecimal totalScore;

    @Schema(description = "自我介绍&个人优势", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("自我介绍&个人优势")
    private String note;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}