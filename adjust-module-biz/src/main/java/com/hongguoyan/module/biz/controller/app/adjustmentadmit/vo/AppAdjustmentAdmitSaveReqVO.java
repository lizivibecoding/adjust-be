package com.hongguoyan.module.biz.controller.app.adjustmentadmit.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Schema(description = "用户 APP - 调剂录取名单新增/修改 Request VO")
@Data
public class AppAdjustmentAdmitSaveReqVO {

    @Schema(description = "调剂录取名单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "21938")
    private Long id;

    @Schema(description = "学校ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "15509")
    @NotNull(message = "学校ID不能为空")
    private Long schoolId;

    @Schema(description = "学校名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "王五")
    @NotEmpty(message = "学校名称不能为空")
    private String schoolName;

    @Schema(description = "学院ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "887")
    @NotNull(message = "学院ID不能为空")
    private Long collegeId;

    @Schema(description = "学院名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "赵六")
    @NotEmpty(message = "学院名称不能为空")
    private String collegeName;

    @Schema(description = "专业ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "27212")
    @NotNull(message = "专业ID不能为空")
    private Long majorId;

    @Schema(description = "专业名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @NotEmpty(message = "专业名称不能为空")
    private String majorName;

    @Schema(description = "专业代码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "专业代码不能为空")
    private String majorCode;

    @Schema(description = "方向ID", example = "27674")
    private Long directionId;

    @Schema(description = "方向名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "赵六")
    @NotEmpty(message = "方向名称不能为空")
    private String directionName;

    @Schema(description = "年份(如2025)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "年份(如2025)不能为空")
    private Short year;

    @Schema(description = "学习方式(全日制/非全日制)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "学习方式(全日制/非全日制)不能为空")
    private String studyMode;

    @Schema(description = "考生名称(脱敏)", requiredMode = Schema.RequiredMode.REQUIRED, example = "芋艿")
    @NotEmpty(message = "考生名称(脱敏)不能为空")
    private String candidateName;

    @Schema(description = "一志愿学校ID", example = "31194")
    private Long firstSchoolId;

    @Schema(description = "一志愿学校名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "王五")
    @NotEmpty(message = "一志愿学校名称不能为空")
    private String firstSchoolName;

    @Schema(description = "初试成绩")
    private BigDecimal firstScore;

    @Schema(description = "复试成绩")
    private BigDecimal retestScore;

    @Schema(description = "总成绩")
    private BigDecimal totalScore;

}