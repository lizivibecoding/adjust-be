package com.hongguoyan.module.biz.controller.app.adjustmentadmit.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import cn.idev.excel.annotation.*;

@Schema(description = "调剂录取名单-响应")
@Data
@ExcelIgnoreUnannotated
public class AppAdjustmentAdmitRespVO {

    @Schema(description = "调剂录取名单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "21938")
    @ExcelProperty("调剂录取名单ID")
    private Long id;

    @Schema(description = "学校ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "15509")
    @ExcelProperty("学校ID")
    private Long schoolId;

    @Schema(description = "学校名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "王五")
    @ExcelProperty("学校名称")
    private String schoolName;

    @Schema(description = "学院ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "887")
    @ExcelProperty("学院ID")
    private Long collegeId;

    @Schema(description = "学院名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "赵六")
    @ExcelProperty("学院名称")
    private String collegeName;

    @Schema(description = "专业ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "27212")
    @ExcelProperty("专业ID")
    private Long majorId;

    @Schema(description = "专业名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @ExcelProperty("专业名称")
    private String majorName;

    @Schema(description = "专业代码", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("专业代码")
    private String majorCode;

    @Schema(description = "方向ID", example = "755")
    @ExcelProperty("方向ID")
    private Long directionId;

    @Schema(description = "方向名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "赵六")
    @ExcelProperty("方向名称")
    private String directionName;

    @Schema(description = "年份", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("年份")
    private Short year;

    @Schema(description = "学习方式：全日制/非全日制", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("学习方式：全日制/非全日制")
    private String studyMode;

    @Schema(description = "考生名称(脱敏)", requiredMode = Schema.RequiredMode.REQUIRED, example = "芋艿")
    @ExcelProperty("考生名称(脱敏)")
    private String candidateName;

    @Schema(description = "一志愿学校ID", example = "31194")
    @ExcelProperty("一志愿学校ID")
    private Long firstSchoolId;

    @Schema(description = "一志愿学校名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "王五")
    @ExcelProperty("一志愿学校名称")
    private String firstSchoolName;

    @Schema(description = "初试成绩")
    @ExcelProperty("初试成绩")
    private BigDecimal firstScore;

    @Schema(description = "复试成绩")
    @ExcelProperty("复试成绩")
    private BigDecimal retestScore;

    @Schema(description = "总成绩")
    @ExcelProperty("总成绩")
    private BigDecimal totalScore;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}