package com.hongguoyan.module.biz.controller.app.school.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "院校调剂列表-响应")
@Data
public class AppSchoolAdjustmentRespVO {

    @Schema(description = "学校ID", example = "5")
    private Long schoolId;

    @Schema(description = "学院ID", example = "95")
    private Long collegeId;

    @Schema(description = "专业ID", example = "2810")
    private Long majorId;

    @Schema(description = "专业代码")
    private String majorCode;

    @Schema(description = "专业名称")
    private String majorName;

    @Schema(description = "学院名称")
    private String collegeName;

    @Schema(description = "年份", example = "2024")
    private Integer year;

    @Schema(description = "学习方式：全日制/非全日制")
    private String studyMode;

    @Schema(description = "招生人数(调剂缺额汇总)", example = "8")
    private Long adjustCount;

}

