package com.hongguoyan.module.biz.controller.app.useradjustment.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "用户 APP - 用户发布调剂创建 Request VO（精简）")
@Data
public class AppUserAdjustmentCreateReqVO {

    @Schema(description = "方向ID(biz_school_direction.id)", requiredMode = Schema.RequiredMode.REQUIRED, example = "28566")
    @NotNull(message = "directionId不能为空")
    private Long directionId;

    @Schema(description = "调剂年份", requiredMode = Schema.RequiredMode.REQUIRED, example = "2025")
    @NotNull(message = "调剂年份不能为空")
    private Integer year;

    @Schema(description = "调剂缺额人数", requiredMode = Schema.RequiredMode.REQUIRED, example = "3")
    @NotNull(message = "调剂缺额人数不能为空")
    private Integer adjustCount;

    @Schema(description = "原计划招生人数/剩余(参考)", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    @NotNull(message = "原计划招生人数/剩余不能为空")
    private Integer adjustLeft;

    @Schema(description = "联系方式(不区分手机/微信)", requiredMode = Schema.RequiredMode.REQUIRED, example = "981453801@qq.com")
    @NotEmpty(message = "联系方式不能为空")
    private String contact;

    @Schema(description = "调剂信息标题", requiredMode = Schema.RequiredMode.REQUIRED, example = "江苏大学招有调剂")
    @NotEmpty(message = "调剂信息标题不能为空")
    private String title;

    @Schema(description = "调剂具体要求", requiredMode = Schema.RequiredMode.REQUIRED, example = "国家线达标")
    @NotEmpty(message = "调剂具体要求不能为空")
    private String remark;
}

