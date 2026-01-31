package com.hongguoyan.module.biz.controller.app.usersubscription.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "用户 APP - 取消订阅 Request VO")
@Data
public class AppUserSubscriptionUnsubscribeReqVO {

    @Schema(description = "学校ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "11324")
    @NotNull(message = "学校ID不能为空")
    private Long schoolId;

    @Schema(description = "专业ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "7206")
    @NotNull(message = "专业ID不能为空")
    private Long majorId;

}

