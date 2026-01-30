package com.hongguoyan.module.biz.controller.app.useradjustment.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "用户 APP - 发布调剂列表 Item Response VO")
@Data
public class AppUserAdjustmentListRespVO {

    @Schema(description = "用户发布调剂ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1558")
    private Long id;

    @Schema(description = "调剂信息标题", requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;

    @Schema(description = "学校名称")
    private String schoolName;

    @Schema(description = "学校Logo")
    private String schoolLogo;

    @Schema(description = "专业一级名称")
    private String majorLevel1Name;

    @Schema(description = "专业代码")
    private String majorCode;

    @Schema(description = "专业名称")
    private String majorName;

    @Schema(description = "学位类型(0未知/不区分 1专硕 2学硕)")
    private Integer degreeType;

    @Schema(description = "调剂缺额人数")
    private Integer adjustCount;

    @Schema(description = "发布时间")
    private LocalDateTime publishTime;
}

