package com.hongguoyan.module.biz.controller.app.vip.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "用户 APP - 我的会员信息 Response VO")
@Data
public class AppVipMeRespVO {

    @Schema(description = "VIP 是否有效", example = "true")
    private Boolean vipValid;

    @Schema(description = "VIP 到期时间")
    private LocalDateTime vipEndTime;

    @Schema(description = "SVIP 是否有效", example = "false")
    private Boolean svipValid;

    @Schema(description = "SVIP 到期时间")
    private LocalDateTime svipEndTime;

    @Schema(description = "到期展示时间（VIP/SVIP 最大 end_time）")
    private LocalDateTime maxEndTime;

    @Schema(description = "当前可用功能 key 列表（并集）")
    private List<String> enabledFeatures;

}

