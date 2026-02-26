package com.hongguoyan.module.biz.controller.admin.useradjustment.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "管理后台 - 运营新增用户发布调剂 Request VO")
@Data
public class UserAdjustmentAdminCreateReqVO {

    @Schema(description = "发布来源(1老师 2学长 3小道消息)", requiredMode = Schema.RequiredMode.REQUIRED, example = "3")
    @NotNull(message = "sourceType不能为空")
    private Integer sourceType;

    @Schema(description = "发布者用户ID（sourceType=老师/学长时必填）", example = "100")
    private Long publisherUserId;

    @Schema(description = "方向ID(biz_school_direction.id)", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "directionId不能为空")
    private Long directionId;

    @Schema(description = "缺额人数", requiredMode = Schema.RequiredMode.REQUIRED, example = "10")
    @NotNull(message = "adjustCount不能为空")
    private Integer adjustCount;

    @Schema(description = "剩余/参考", example = "0")
    private Integer adjustLeft;

    @Schema(description = "联系方式(不区分手机/微信)")
    private String contact;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "备注")
    private String remark;
}

