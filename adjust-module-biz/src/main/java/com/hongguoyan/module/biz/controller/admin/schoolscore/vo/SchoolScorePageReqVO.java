package com.hongguoyan.module.biz.controller.admin.schoolscore.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.hongguoyan.framework.common.pojo.PageParam;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static com.hongguoyan.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 自划线分页 Request VO")
@Data
public class SchoolScorePageReqVO extends PageParam {

    @Schema(description = "学校ID(biz_school.id)", example = "22023")
    private Long schoolId;

    @Schema(description = "学校名称(冗余)", example = "王五")
    private String schoolName;

    @Schema(description = "学院名称(关键区分字段)", example = "张三")
    private String collegeName;

    @Schema(description = "专业ID(biz_major.id)", example = "26101")
    private Long majorId;

    @Schema(description = "专业代码(如010102)")
    private String majorCode;

    @Schema(description = "专业名称", example = "王五")
    private String majorName;

    @Schema(description = "学位类型(0=不区分,1=专硕,2=学硕)", example = "1")
    private Integer degreeType;

    @Schema(description = "年份(如2025)")
    private Short year;

    @Schema(description = "政治")
    private BigDecimal scoreSubject1;

    @Schema(description = "外语")
    private BigDecimal scoreSubject2;

    @Schema(description = "业务课1")
    private BigDecimal scoreSubject3;

    @Schema(description = "业务课2")
    private BigDecimal scoreSubject4;

    @Schema(description = "总分")
    private BigDecimal scoreTotal;

    @Schema(description = "备注", example = "你说的对")
    private String remark;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}