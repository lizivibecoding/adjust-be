package com.hongguoyan.module.biz.controller.app.vip.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = "会员套餐-响应")
@Data
public class AppVipPlanRespVO {

    @Schema(description = "套餐编码", example = "VIP")
    private String planCode;

    @Schema(description = "套餐名称", example = "VIP 月卡")
    private String planName;

    @Schema(description = "价格（单位：分）", example = "9900")
    private Integer planPrice;

    @Schema(description = "时长（单位：天）", example = "30")
    private Integer durationDays;

    @Schema(description = "排序", example = "10")
    private Integer sort;

    @Schema(description = "权益点（用于展示）")
    private List<AppVipPlanBenefitRespVO> benefits;

}

