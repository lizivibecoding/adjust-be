package com.hongguoyan.module.biz.controller.app.schoolcollege.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.hongguoyan.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static com.hongguoyan.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "用户 APP - 学院分页 Request VO")
@Data
public class AppSchoolCollegePageReqVO extends PageParam {

    @Schema(description = "学校ID(biz_school.id)", example = "1694")
    private Long schoolId;

    @Schema(description = "院系代码")
    private String code;

    @Schema(description = "院系名称", example = "张三")
    private String name;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}