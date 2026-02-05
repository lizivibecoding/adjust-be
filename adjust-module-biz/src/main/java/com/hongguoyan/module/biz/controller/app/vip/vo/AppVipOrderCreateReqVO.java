package com.hongguoyan.module.biz.controller.app.vip.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Schema(description = "创建会员订单-请求")
@Data
public class AppVipOrderCreateReqVO {

    @Schema(description = "套餐编码：VIP/SVIP", requiredMode = Schema.RequiredMode.REQUIRED, example = "VIP")
    @NotBlank(message = "planCode 不能为空")
    private String planCode;

}

