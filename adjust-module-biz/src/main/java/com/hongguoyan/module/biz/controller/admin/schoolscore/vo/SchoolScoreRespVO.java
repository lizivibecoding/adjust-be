package com.hongguoyan.module.biz.controller.admin.schoolscore.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import cn.idev.excel.annotation.*;

@Schema(description = "管理后台 - 自划线 Response VO")
@Data
@ExcelIgnoreUnannotated
public class SchoolScoreRespVO {

    @Schema(description = "院校分数线ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "20049")
    @ExcelProperty("院校分数线ID")
    private Long id;

    @Schema(description = "学校ID(biz_school.id)", requiredMode = Schema.RequiredMode.REQUIRED, example = "22023")
    @ExcelProperty("学校ID(biz_school.id)")
    private Long schoolId;

    @Schema(description = "学校名称(冗余)", requiredMode = Schema.RequiredMode.REQUIRED, example = "王五")
    @ExcelProperty("学校名称(冗余)")
    private String schoolName;

    @Schema(description = "学院名称(关键区分字段)", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @ExcelProperty("学院名称(关键区分字段)")
    private String collegeName;

    @Schema(description = "专业ID(biz_major.id)", example = "26101")
    @ExcelProperty("专业ID(biz_major.id)")
    private Long majorId;

    @Schema(description = "专业代码(如010102)", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("专业代码(如010102)")
    private String majorCode;

    @Schema(description = "专业名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "王五")
    @ExcelProperty("专业名称")
    private String majorName;

    @Schema(description = "学位类型(0=不区分,1=专硕,2=学硕)", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("学位类型(0=不区分,1=专硕,2=学硕)")
    private Integer degreeType;

    @Schema(description = "年份(如2025)", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("年份(如2025)")
    private Short year;

    @Schema(description = "政治", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("政治")
    private BigDecimal scoreSubject1;

    @Schema(description = "外语", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("外语")
    private BigDecimal scoreSubject2;

    @Schema(description = "业务课1", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("业务课1")
    private BigDecimal scoreSubject3;

    @Schema(description = "业务课2", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("业务课2")
    private BigDecimal scoreSubject4;

    @Schema(description = "总分", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("总分")
    private BigDecimal scoreTotal;

    @Schema(description = "备注", example = "你说的对")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}