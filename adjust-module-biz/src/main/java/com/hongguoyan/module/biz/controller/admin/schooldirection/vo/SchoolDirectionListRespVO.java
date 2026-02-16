package com.hongguoyan.module.biz.controller.admin.schooldirection.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "管理后台 - 院校研究方向列表 Response VO")
@Data
public class SchoolDirectionListRespVO {

    @Schema(description = "记录ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "学校ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "5")
    private Long schoolId;

    @Schema(description = "学院ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "95")
    private Long collegeId;

    @Schema(description = "专业ID(biz_major.id)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2810")
    private Long majorId;

    @Schema(description = "方向代码", example = "01")
    private String directionCode;

    @Schema(description = "方向名称", example = "人工智能")
    private String directionName;

    @Schema(description = "学习方式：1-全日制 2-非全日制", example = "1")
    private Integer studyMode;

    @Schema(description = "是否退役大学生计划：0-否 1-是", example = "0")
    private Integer retiredPlan;

    @Schema(description = "是否少数民族计划：0-否 1-是", example = "0")
    private Integer shaoGuPlan;

    @Schema(description = "考试科目 JSON")
    private String subjects;
}

