package com.hongguoyan.module.biz.controller.app.adjustmentadmit.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.hongguoyan.framework.common.pojo.PageParam;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static com.hongguoyan.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "调剂录取名单-分页-请求")
@Data
public class AppAdjustmentAdmitPageReqVO extends PageParam {

    @Schema(description = "学校ID", example = "5")
    private Long schoolId;

    @Schema(description = "学校名称", example = "王五")
    private String schoolName;

    @Schema(description = "学院ID", example = "95")
    private Long collegeId;

    @Schema(description = "学院名称", example = "赵六")
    private String collegeName;

    @Schema(description = "专业ID", example = "2810")
    private Long majorId;

    @Schema(description = "专业名称", example = "张三")
    private String majorName;

    @Schema(description = "专业代码")
    private String majorCode;

    @Schema(description = "方向ID", example = "755")
    private Long directionId;

    @Schema(description = "方向名称", example = "赵六")
    private String directionName;

    @Schema(description = "年份")
    private Short year;

    @Schema(description = "学习方式：1-全日制 2-非全日制", example = "1")
    private Integer studyMode;

    @Schema(description = "考生名称(脱敏)", example = "芋艿")
    private String candidateName;

    @Schema(description = "一志愿学校ID", example = "31194")
    private Long firstSchoolId;

    @Schema(description = "一志愿学校名称", example = "王五")
    private String firstSchoolName;

    @Schema(description = "初试成绩")
    private BigDecimal firstScore;

    @Schema(description = "复试成绩")
    private BigDecimal retestScore;

    @Schema(description = "总成绩")
    private BigDecimal totalScore;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}