package com.hongguoyan.module.biz.controller.app.schoolmajor.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import jakarta.validation.constraints.*;

@Schema(description = "管理后台 - 院校专业新增/修改 Request VO")
@Data
public class SchoolMajorSaveReqVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "5954")
    private Long id;

    @Schema(description = "学校ID(biz_school.id)", requiredMode = Schema.RequiredMode.REQUIRED, example = "15188")
    @NotNull(message = "学校ID(biz_school.id)不能为空")
    private Long schoolId;

    @Schema(description = "院系ID(biz_school_college.id)", requiredMode = Schema.RequiredMode.REQUIRED, example = "24519")
    @NotNull(message = "院系ID(biz_school_college.id)不能为空")
    private Long collegeId;

    @Schema(description = "专业ID(biz_special.id, level=3)", requiredMode = Schema.RequiredMode.REQUIRED, example = "18755")
    @NotNull(message = "专业ID(biz_special.id, level=3)不能为空")
    private Long majorId;

    @Schema(description = "专业代码(来源enroll 专业列括号内)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "专业代码(来源enroll 专业列括号内)不能为空")
    private String code;

    @Schema(description = "专业名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @NotEmpty(message = "专业名称不能为空")
    private String name;

    @Schema(description = "学位类型(0=未知/不区分,1=专硕,2=学硕)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "学位类型(0=未知/不区分,1=专硕,2=学硕)不能为空")
    private Integer degreeType;

    @Schema(description = "热度值", requiredMode = Schema.RequiredMode.REQUIRED, example = "30516")
    @NotNull(message = "热度值不能为空")
    private Integer viewCount;

}