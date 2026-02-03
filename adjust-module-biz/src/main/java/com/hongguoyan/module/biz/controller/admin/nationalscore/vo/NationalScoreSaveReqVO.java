package com.hongguoyan.module.biz.controller.admin.nationalscore.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import jakarta.validation.constraints.*;

@Schema(description = "管理后台 - 国家线新增/修改 Request VO")
@Data
public class NationalScoreSaveReqVO {

    @Schema(description = "国家线ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "20399")
    private Long id;

    @Schema(description = "年份", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "年份不能为空")
    private Integer year;

    @Schema(description = "学位类型(0=不区分,1=专硕,2=学硕)", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "学位类型(0=不区分,1=专硕,2=学硕)不能为空")
    private Integer degreeType;

    @Schema(description = "考研分区(A/B)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "考研分区(A/B)不能为空")
    private String area;

    @Schema(description = "分数线类型(1=普通线,2=少数民族线)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "分数线类型(1=普通线,2=少数民族线)不能为空")
    private Integer scoreType;

    @Schema(description = "专业ID(biz_major.id)", example = "23066")
    private Long majorId;

    @Schema(description = "专业代码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "专业代码不能为空")
    private String majorCode;

    @Schema(description = "专业名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @NotEmpty(message = "专业名称不能为空")
    private String majorName;

    @Schema(description = "总分", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "总分不能为空")
    private Short total;

    @Schema(description = "单科(满分=100)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "单科(满分=100)不能为空")
    private Short single100;

    @Schema(description = "单科(满分=150)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "单科(满分=150)不能为空")
    private Short single150;

}