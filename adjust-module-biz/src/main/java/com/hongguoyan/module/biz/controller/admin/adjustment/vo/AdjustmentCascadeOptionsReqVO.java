package com.hongguoyan.module.biz.controller.admin.adjustment.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 调剂联动选项 Request VO（固定 activeYear）")
@Data
public class AdjustmentCascadeOptionsReqVO {

    @Schema(description = "学校搜索关键字（用于学校下拉）", example = "交通")
    private String keyword;

    @Schema(description = "学校ID", example = "5")
    private Long schoolId;

    @Schema(description = "学院ID", example = "95")
    private Long collegeId;

    @Schema(description = "专业ID(biz_major.id)", example = "2810")
    private Long majorId;

    @Schema(description = "学习方式：1-全日制 2-非全日制", example = "1")
    private Integer studyMode;
}

