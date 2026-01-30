package com.hongguoyan.module.biz.controller.app.adjustment.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "用户 APP - 学校联动选项 Request VO")
@Data
public class AppSchoolSpecialOptionsReqVO {

    @Schema(description = "类型(1=学院,2=专业+学习方式,3=研究方向,4=分数线)", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "类型不能为空")
    private Integer type;

    @Schema(description = "学校ID(type=1/2/3 时需要)", example = "9101")
    private Long schoolId;

    @Schema(description = "学院ID(type=2/3 时需要)", example = "6746")
    private Long collegeId;

    @Schema(description = "专业ID(type=3 时需要；type=2 返回专业列表不需要传)", example = "15937")
    private Long majorId;

    @Schema(description = "学习方式(type=3 可选；不传则返回全部学习方式的方向)", example = "全日制")
    private String studyMode;

    @Schema(description = "方向ID(type=4 可传；不传则用 schoolId+majorId)", example = "20106")
    private Long directionId;
}

