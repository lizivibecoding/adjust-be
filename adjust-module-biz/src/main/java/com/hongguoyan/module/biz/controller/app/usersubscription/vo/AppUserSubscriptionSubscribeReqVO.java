package com.hongguoyan.module.biz.controller.app.usersubscription.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "用户 APP - 订阅调剂 Request VO")
@Data
public class AppUserSubscriptionSubscribeReqVO {

    @Schema(description = "调剂ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotNull(message = "调剂ID不能为空")
    private Long adjustmentId;

}

