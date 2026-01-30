package com.hongguoyan.module.biz.controller.app.useradjustmentapply.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.hongguoyan.framework.common.pojo.PageParam;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static com.hongguoyan.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "用户 APP - 用户发布调剂申请记录分页 Request VO")
@Data
public class AppUserAdjustmentApplyPageReqVO extends PageParam {

    @Schema(description = "用户发布调剂ID(biz_user_adjustment.id)", example = "2789")
    private Long userAdjustmentId;

    @Schema(description = "申请人用户ID(member.user.id)", example = "1968")
    private Long userId;

    @Schema(description = "姓名", example = "王五")
    private String candidateName;

    @Schema(description = "联系方式(手机号/微信)")
    private String contact;

    @Schema(description = "一志愿报考学校ID(biz_school.id)", example = "15421")
    private Long firstSchoolId;

    @Schema(description = "一志愿报考学校名称(冗余)", example = "芋艿")
    private String firstSchoolName;

    @Schema(description = "一志愿报考专业ID(biz_major.id)", example = "13734")
    private Long firstMajorId;

    @Schema(description = "一志愿报考专业代码(冗余)")
    private String firstMajorCode;

    @Schema(description = "一志愿报考专业名称(冗余)", example = "芋艿")
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

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}