package com.hongguoyan.module.biz.controller.app.schoolmajor.vo;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.hongguoyan.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static com.hongguoyan.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 院校专业分页 Request VO")
@Data
public class SchoolMajorPageReqVO extends PageParam {

    @Schema(description = "学校ID(biz_school.id)", example = "15188")
    private Long schoolId;

    @Schema(description = "院系ID(biz_school_college.id)", example = "24519")
    private Long collegeId;

    @Schema(description = "专业ID(biz_special.id, level=3)", example = "18755")
    private Long majorId;

    @Schema(description = "专业代码(来源enroll 专业列括号内)")
    private String code;

    @Schema(description = "专业名称", example = "张三")
    private String name;

    @Schema(description = "学位类型(0=未知/不区分,1=专硕,2=学硕)", example = "2")
    private Integer degreeType;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "热度值", example = "30516")
    private Integer viewCount;

}