package com.hongguoyan.module.biz.controller.app.usersubscription.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "订阅未读状态-响应")
@Data
public class AppUserSubscriptionUnreadRespVO {

    @Schema(description = "是否存在未读更新", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
    private Boolean hasUnread;

}

