package com.hongguoyan.module.biz.controller.app.adjustment.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "用户 APP - 调剂详情切换学院选项 Response VO")
@Data
public class AppAdjustmentCollegeOptionRespVO {

    @Schema(description = "学院ID", example = "6746")
    private Long collegeId;

    @Schema(description = "学院名称", example = "基础医学院")
    private String collegeName;

}

