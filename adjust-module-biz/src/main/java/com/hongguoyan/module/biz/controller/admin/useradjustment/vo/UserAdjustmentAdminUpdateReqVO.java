package com.hongguoyan.module.biz.controller.admin.useradjustment.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "管理后台 - 用户发布调剂（硕士招生）更新 Request VO")
@Data
public class UserAdjustmentAdminUpdateReqVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "id不能为空")
    private Long id;

    @Schema(description = "方向ID(biz_school_direction.id)", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "directionId不能为空")
    private Long directionId;

    @Schema(description = "缺额人数", requiredMode = Schema.RequiredMode.REQUIRED, example = "10")
    @NotNull(message = "adjustCount不能为空")
    private Integer adjustCount;

    @Schema(description = "剩余名额/参考", example = "0")
    private Integer adjustLeft;

    @Schema(description = "联系方式(不区分手机/微信)")
    private String contact;

    @Schema(description = "来源URL/原文链接")
    private String sourceUrl;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "备注")
    private String remark;
}

