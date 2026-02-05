package com.hongguoyan.module.biz.controller.app.schoolcollege.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.hongguoyan.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static com.hongguoyan.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "学院-分页-请求")
@Data
public class AppSchoolCollegePageReqVO extends PageParam {

    @Schema(description = "学校ID", example = "5")
    private Long schoolId;

    @Schema(description = "院系代码")
    private String code;

    @Schema(description = "院系名称", example = "张三")
    private String name;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}