package com.hongguoyan.module.biz.controller.app.usersubscription.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "用户 APP - 订阅未读状态 Response VO")
@Data
public class AppUserSubscriptionUnreadRespVO {

    @Schema(description = "是否存在未读更新(小铃铛红点)", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
    private Boolean hasUnread;

}

