package com.hongguoyan.module.biz.controller.app.vip.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "用户 APP - 会员套餐权益点 Response VO")
@Data
public class AppVipPlanFeatureRespVO {

    @Schema(description = "功能点 key", example = "view_history_list")
    private String featureKey;

    @Schema(description = "权益点名称")
    private String name;

    @Schema(description = "权益点描述")
    private String description;

    @Schema(description = "排序", example = "10")
    private Integer sort;

    @Schema(description = "功能开关：0 关闭，1 开启", example = "1")
    private Integer enabled;

}

