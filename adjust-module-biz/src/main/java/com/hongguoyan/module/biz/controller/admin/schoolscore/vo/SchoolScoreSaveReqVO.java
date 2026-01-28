package com.hongguoyan.module.biz.controller.admin.schoolscore.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Schema(description = "管理后台 - 自划线新增/修改 Request VO")
@Data
public class SchoolScoreSaveReqVO {

    @Schema(description = "院校分数线ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "20049")
    private Long id;

    @Schema(description = "学校ID(biz_school.id)", requiredMode = Schema.RequiredMode.REQUIRED, example = "22023")
    @NotNull(message = "学校ID(biz_school.id)不能为空")
    private Long schoolId;

    @Schema(description = "学校名称(冗余)", requiredMode = Schema.RequiredMode.REQUIRED, example = "王五")
    @NotEmpty(message = "学校名称(冗余)不能为空")
    private String schoolName;

    @Schema(description = "学院名称(关键区分字段)", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @NotEmpty(message = "学院名称(关键区分字段)不能为空")
    private String collegeName;

    @Schema(description = "专业ID(biz_major.id)", example = "26101")
    private Long majorId;

    @Schema(description = "专业代码(如010102)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "专业代码(如010102)不能为空")
    private String majorCode;

    @Schema(description = "专业名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "王五")
    @NotEmpty(message = "专业名称不能为空")
    private String majorName;

    @Schema(description = "学位类型(0=不区分,1=专硕,2=学硕)", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "学位类型(0=不区分,1=专硕,2=学硕)不能为空")
    private Integer degreeType;

    @Schema(description = "年份(如2025)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "年份(如2025)不能为空")
    private Short year;

    @Schema(description = "政治", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "政治不能为空")
    private BigDecimal scoreSubject1;

    @Schema(description = "外语", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "外语不能为空")
    private BigDecimal scoreSubject2;

    @Schema(description = "业务课1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "业务课1不能为空")
    private BigDecimal scoreSubject3;

    @Schema(description = "业务课2", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "业务课2不能为空")
    private BigDecimal scoreSubject4;

    @Schema(description = "总分", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "总分不能为空")
    private BigDecimal scoreTotal;

    @Schema(description = "备注", example = "你说的对")
    private String remark;

}