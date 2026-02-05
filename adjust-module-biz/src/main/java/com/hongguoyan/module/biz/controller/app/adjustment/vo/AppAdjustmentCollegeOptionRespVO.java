package com.hongguoyan.module.biz.controller.app.adjustment.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "调剂详情切换学院选项-响应")
@Data
public class AppAdjustmentCollegeOptionRespVO {

    @Schema(description = "学院ID", example = "95")
    private Long collegeId;

    @Schema(description = "学院名称", example = "基础医学院")
    private String collegeName;

}

