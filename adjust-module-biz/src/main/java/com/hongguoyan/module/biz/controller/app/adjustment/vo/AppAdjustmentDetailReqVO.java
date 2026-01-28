package com.hongguoyan.module.biz.controller.app.adjustment.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "用户 APP - 调剂详情 Request VO")
@Data
public class AppAdjustmentDetailReqVO {

    @Schema(description = "学校ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "9101")
    @NotNull(message = "学校ID不能为空")
    private Long schoolId;

    @Schema(description = "专业ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "15937")
    @NotNull(message = "专业ID不能为空")
    private Long majorId;

    @Schema(description = "学院ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "6746")
    @NotNull(message = "学院ID不能为空")
    private Long collegeId;

    @Schema(description = "调剂年份", requiredMode = Schema.RequiredMode.REQUIRED, example = "2025")
    @NotNull(message = "调剂年份不能为空")
    private Integer year;

    @Schema(description = "学习方式(全日制/非全日制)", requiredMode = Schema.RequiredMode.REQUIRED, example = "全日制")
    @NotBlank(message = "学习方式不能为空")
    private String studyMode;
}

