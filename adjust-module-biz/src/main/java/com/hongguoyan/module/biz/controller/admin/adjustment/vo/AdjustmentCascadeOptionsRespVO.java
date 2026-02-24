package com.hongguoyan.module.biz.controller.admin.adjustment.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = "管理后台 - 调剂联动选项 Response VO（固定 activeYear）")
@Data
public class AdjustmentCascadeOptionsRespVO {

    @Schema(description = "学校列表（keyword 搜索时返回）")
    private List<SchoolOption> schools;

    @Schema(description = "学院列表（schoolId 时返回）")
    private List<CollegeOption> colleges;

    @Schema(description = "专业列表（schoolId+collegeId 时返回）")
    private List<MajorOption> majors;

    @Schema(description = "学习方式列表（schoolId+collegeId+majorId 时返回）")
    private List<StudyModeOption> studyModes;

    @Schema(description = "方向列表（schoolId+collegeId+majorId 时返回；studyMode 传入则过滤）")
    private List<DirectionOption> directions;

    @Data
    public static class SchoolOption {
        @Schema(description = "学校ID", example = "5")
        private Long id;
        @Schema(description = "学校名称", example = "上海交通大学")
        private String name;
    }

    @Data
    public static class CollegeOption {
        @Schema(description = "学院ID", example = "95")
        private Long id;
        @Schema(description = "学院名称", example = "计算机学院")
        private String name;
    }

    @Data
    public static class MajorOption {
        @Schema(description = "专业ID(biz_major.id)", example = "2810")
        private Long majorId;
        @Schema(description = "专业代码", example = "085400")
        private String code;
        @Schema(description = "专业名称", example = "电子信息")
        private String name;
        @Schema(description = "学位类型(0=不区分,1=学硕,2=专硕)", example = "2")
        private Integer degreeType;
    }

    @Data
    public static class StudyModeOption {
        @Schema(description = "学习方式 code", example = "1")
        private Integer code;
        @Schema(description = "学习方式名称", example = "全日制")
        private String name;
    }

    @Data
    public static class DirectionOption {
        @Schema(description = "方向ID(biz_school_direction.id)", example = "755")
        private Long id;
        @Schema(description = "方向代码", example = "01")
        private String directionCode;
        @Schema(description = "方向名称", example = "人工智能")
        private String directionName;
        @Schema(description = "学习方式：1-全日制 2-非全日制", example = "1")
        private Integer studyMode;
    }
}

