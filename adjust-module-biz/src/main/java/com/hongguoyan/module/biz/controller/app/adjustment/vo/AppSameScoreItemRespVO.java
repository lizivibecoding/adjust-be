package com.hongguoyan.module.biz.controller.app.adjustment.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
import com.hongguoyan.module.biz.framework.jackson.StudyModeNameSerializer;
import lombok.Data;

@Schema(description = "同分调剂去向列表-项-响应")
@Data
public class AppSameScoreItemRespVO {

    @Schema(description = "学校ID", example = "436")
    private Long schoolId;

    @Schema(description = "学校名称", example = "内蒙古科技大学")
    private String schoolName;

    @Schema(description = "省份名称", example = "内蒙古")
    private String provinceName;

    @Schema(description = "考研分区：A区/B区", example = "B")
    private String provinceArea;

    @Schema(description = "学校层次(1=985,2=211(不含985),3=双一流(不含985、211),4=普通)", example = "4")
    private Integer schoolLevel;

    @Schema(description = "学校层次名称", example = "普通院校")
    private String schoolLevelName;

    @Schema(description = "学院ID", example = "32883")
    private Long collegeId;

    @Schema(description = "学院名称", example = "包头师范学院信息科学与技术学院")
    private String collegeName;

    @Schema(description = "专业ID", example = "20471")
    private Long majorId;

    @Schema(description = "专业代码", example = "085404")
    private String majorCode;

    @Schema(description = "专业名称", example = "计算机技术")
    private String majorName;

    @Schema(description = "年份", example = "2025")
    private Integer year;

    @Schema(description = "学习方式", example = "全日制", type = "string")
    @JsonSerialize(using = StudyModeNameSerializer.class)
    private Integer studyMode;

    @Schema(description = "初试分数(同分区间内该去向的最高初试分)", example = "298")
    private Integer firstScore;
}

