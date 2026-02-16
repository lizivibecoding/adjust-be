package com.hongguoyan.module.biz.controller.admin.vipcoupon.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 会员券码 Response VO")
@Data
public class VipCouponRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "31822")
    private Long id;

    @Schema(description = "券码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String code;

    @Schema(description = "批次号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String batchNo;

    @Schema(description = "套餐编码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String planCode;

    @Schema(description = "状态：1未使用,2已使用,3过期,4作废", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer status;

    @Schema(description = "开始时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime validStartTime;

    @Schema(description = "截止时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime validEndTime;

    @Schema(description = "使用人ID", example = "27020")
    private Long userId;

    @Schema(description = "使用时间")
    private LocalDateTime usedTime;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

    @Schema(description = "用户昵称")
    private String userNickname;

    @Schema(description = "用户手机号")
    private String userMobile;

    @Schema(description = "用户头像")
    private String userAvatar;

}

