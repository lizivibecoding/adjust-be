package com.hongguoyan.module.biz.controller.admin.vipplan.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "管理后台 - 套餐卡片 Response VO")
@Data
public class VipPlanCardRespVO {

    @Schema(description = "套餐编码：VIP / SVIP", requiredMode = Schema.RequiredMode.REQUIRED)
    private String planCode;

    @Schema(description = "套餐名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String planName;

    @Schema(description = "价格（单位：分）", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer planPrice;

    @Schema(description = "周期（单位：天）", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer durationDays;

    @Schema(description = "活动价（单位：分）")
    private Integer discountPrice;

    @Schema(description = "活动开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime discountStartTime;

    @Schema(description = "活动结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime discountEndTime;

    @Schema(description = "权益列表", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<VipPlanBenefitItemRespVO> benefits;

}
