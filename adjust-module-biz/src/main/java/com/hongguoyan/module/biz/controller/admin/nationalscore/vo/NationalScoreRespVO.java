package com.hongguoyan.module.biz.controller.admin.nationalscore.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import cn.idev.excel.annotation.*;

@Schema(description = "管理后台 - 国家线 Response VO")
@Data
@ExcelIgnoreUnannotated
public class NationalScoreRespVO {

    @Schema(description = "国家线ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "20399")
    @ExcelProperty("国家线ID")
    private Long id;

    @Schema(description = "年份", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("年份")
    private Integer year;

    @Schema(description = "学位类型（0-不区分 1-学硕 2-专硕）", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("学位类型（0-不区分 1-学硕 2-专硕）")
    private Integer degreeType;

    @Schema(description = "考研分区(A/B)", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("考研分区(A/B)")
    private String area;

    @Schema(description = "分数线类型(1=普通线,2=少数民族线)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty("分数线类型(1=普通线,2=少数民族线)")
    private Integer scoreType;

    @Schema(description = "专业ID(biz_major.id)", example = "23066")
    @ExcelProperty("专业ID(biz_major.id)")
    private Long majorId;

    @Schema(description = "专业代码", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("专业代码")
    private String majorCode;

    @Schema(description = "专业名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @ExcelProperty("专业名称")
    private String majorName;

    @Schema(description = "总分", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("总分")
    private Short total;

    @Schema(description = "单科(满分=100)", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("单科(满分=100)")
    private Short single100;

    @Schema(description = "单科(满分=150)", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("单科(满分=150)")
    private Short single150;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}