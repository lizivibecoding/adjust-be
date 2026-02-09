package com.hongguoyan.module.biz.controller.app.userpreference.vo;

import com.hongguoyan.module.biz.enums.StudyModeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "用户志愿-条目响应")
@Data
public class AppUserPreferenceItemRespVO {

    @Schema(description = "条目ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "10001")
    private Long id;

    @Schema(description = "学校名称", example = "大连海洋大学")
    private String schoolName;

    @Schema(description = "学院名称", example = "信息工程学院")
    private String collegeName;

    @Schema(description = "专业代码", example = "085404")
    private String majorCode;

    @Schema(description = "专业名称", example = "计算机技术")
    private String majorName;

    @Schema(description = "方向ID", example = "755")
    private Long directionId;

    @Schema(description = "方向名称", example = "人工智能")
    private String directionName;

    @Schema(description = "学习方式：1-全日制 2-非全日制")
    private Integer studyMode;

    @Schema(description = "来源调剂ID(biz_adjustment.id)", example = "123456")
    private Long sourceAdjustmentId;

    @Schema(description = "学习方式名称", example = "全日制")
    public String getStudyModeName() {
        return StudyModeEnum.getName(studyMode);
    }
}

