package com.hongguoyan.module.biz.controller.admin.schoolcollege.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "管理后台 - 学院列表 Response VO")
@Data
public class SchoolCollegeListRespVO {

    @Schema(description = "学院ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "95")
    private Long id;

    @Schema(description = "学校ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "5")
    private Long schoolId;

    @Schema(description = "学院代码", example = "001")
    private String code;

    @Schema(description = "学院名称", example = "计算机学院")
    private String name;

    @Schema(description = "年份", example = "2026")
    private Integer year;
}

