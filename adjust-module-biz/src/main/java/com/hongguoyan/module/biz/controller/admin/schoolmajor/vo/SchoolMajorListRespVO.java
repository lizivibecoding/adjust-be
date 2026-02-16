package com.hongguoyan.module.biz.controller.admin.schoolmajor.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "管理后台 - 院校专业列表 Response VO")
@Data
public class SchoolMajorListRespVO {

    @Schema(description = "记录ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "学校ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "5")
    private Long schoolId;

    @Schema(description = "学院ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "95")
    private Long collegeId;

    @Schema(description = "专业ID(biz_major.id)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2810")
    private Long majorId;

    @Schema(description = "专业代码", example = "081200")
    private String code;

    @Schema(description = "专业名称", example = "计算机科学与技术")
    private String name;

    @Schema(description = "学位类型(0=不区分,1=学硕,2=专硕)", example = "1")
    private Integer degreeType;

    @Schema(description = "年份", example = "2026")
    private Integer year;
}

