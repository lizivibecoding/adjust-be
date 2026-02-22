package com.hongguoyan.module.biz.controller.app.vip.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "套餐权益-响应")
@Data
public class AppVipPlanBenefitRespVO {

    @Schema(description = "权益 key", example = "user_report")
    private String benefitKey;

    @Schema(description = "权益名称", example = "报告")
    private String benefitName;

    @Schema(description = "权益描述（原始配置，不拼接次数）", example = "报告权益说明")
    private String benefitDesc;

    @Schema(description = "权益类型：1=BOOLEAN 2=QUOTA(次数) 3=LIMIT(阈值)", example = "2")
    private Integer benefitType;

    @Schema(description = "权益数值（如 1/3/8；-1=不限；BOOLEAN 可为空）", example = "3")
    private Integer benefitValue;

}

