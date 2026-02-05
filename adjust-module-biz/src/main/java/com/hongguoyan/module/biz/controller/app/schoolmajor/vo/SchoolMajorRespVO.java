package com.hongguoyan.module.biz.controller.app.schoolmajor.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import cn.idev.excel.annotation.*;

@Schema(description = "管理后台 - 院校专业 Response VO")
@Data
@ExcelIgnoreUnannotated
public class SchoolMajorRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "5954")
    @ExcelProperty("主键")
    private Long id;

    @Schema(description = "学校ID(biz_school.id)", requiredMode = Schema.RequiredMode.REQUIRED, example = "15188")
    @ExcelProperty("学校ID(biz_school.id)")
    private Long schoolId;

    @Schema(description = "院系ID(biz_school_college.id)", requiredMode = Schema.RequiredMode.REQUIRED, example = "24519")
    @ExcelProperty("院系ID(biz_school_college.id)")
    private Long collegeId;

    @Schema(description = "专业ID(biz_special.id, level=3)", requiredMode = Schema.RequiredMode.REQUIRED, example = "18755")
    @ExcelProperty("专业ID(biz_special.id, level=3)")
    private Long majorId;

    @Schema(description = "专业代码(来源enroll 专业列括号内)", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("专业代码(来源enroll 专业列括号内)")
    private String code;

    @Schema(description = "专业名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @ExcelProperty("专业名称")
    private String name;

    @Schema(description = "学位类型（0-不区分 1-学硕 2-专硕）", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("学位类型（0-不区分 1-学硕 2-专硕）")
    private Integer degreeType;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "热度值", requiredMode = Schema.RequiredMode.REQUIRED, example = "30516")
    @ExcelProperty("热度值")
    private Integer viewCount;

}