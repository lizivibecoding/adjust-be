package com.hongguoyan.module.biz.controller.app.vip.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = "用户 APP - 我的权益明细 Response VO")
@Data
public class AppVipBenefitMeRespVO {

    @Schema(description = "当前参与计算的套餐编码（总是包含 FREE）", example = "[\"FREE\",\"VIP\"]")
    private List<String> planCodes;

    @Schema(description = "权益列表（用于前端展示余额/剩余）")
    private List<AppVipBenefitMeItemRespVO> benefits;
}

