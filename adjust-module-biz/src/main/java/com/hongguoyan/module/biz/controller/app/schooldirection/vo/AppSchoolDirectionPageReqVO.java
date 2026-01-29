package com.hongguoyan.module.biz.controller.app.schooldirection.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.hongguoyan.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static com.hongguoyan.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "用户 APP - 院校研究方向分页 Request VO")
@Data
public class AppSchoolDirectionPageReqVO extends PageParam {

    @Schema(description = "学校ID", example = "15236")
    private Long schoolId;

    @Schema(description = "学院ID", example = "927")
    private Long collegeId;

    @Schema(description = "专业ID", example = "27111")
    private Long majorId;

    @Schema(description = "学习方式")
    private String studyMode;

    @Schema(description = "方向代码")
    private String directionCode;

    @Schema(description = "方向名称", example = "李四")
    private String directionName;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}