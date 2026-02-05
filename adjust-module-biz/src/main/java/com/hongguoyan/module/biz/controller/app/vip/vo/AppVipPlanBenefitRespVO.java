package com.hongguoyan.module.biz.controller.app.vip.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "套餐权益-响应")
@Data
public class AppVipPlanBenefitRespVO {

    @Schema(description = "权益 key", example = "view_analysis")
    private String benefitKey;

    @Schema(description = "权益名称")
    private String benefitName;

    @Schema(description = "权益描述")
    private String benefitDesc;

    @Schema(description = "权益类型：1=BOOLEAN 2=QUOTA(次数) 3=LIMIT(阈值)", example = "1")
    private Integer benefitType;

    @Schema(description = "数值（如 1/3/8；-1=不限）", example = "3")
    private Integer benefitValue;

    @Schema(description = "周期：0=NONE", example = "0")
    private Integer periodType;

    @Schema(description = "计次策略：1=COUNT(每次计) 2=UNIQUE_KEY(按唯一键去重)", example = "1")
    private Integer consumePolicy;

    @Schema(description = "排序", example = "10")
    private Integer sort;

    @Schema(description = "功能开关：0 关闭，1 开启", example = "1")
    private Integer enabled;

}

