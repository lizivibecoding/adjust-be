package com.hongguoyan.module.biz.controller.admin.vipsubscription.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 用户会员订阅新增/修改 Request VO")
@Data
public class VipSubscriptionSaveReqVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "27677")
    private Long id;

    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "8932")
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @Schema(description = "套餐编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "套餐编码不能为空")
    private String planCode;

    @Schema(description = "首次开通时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "首次开通时间不能为空")
    private LocalDateTime startTime;

    @Schema(description = "当前到期时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "当前到期时间不能为空")
    private LocalDateTime endTime;

    @Schema(description = "最近一次续期来源：1 支付，2 券码，3 后台", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "最近一次续期来源：1 支付，2 券码，3 后台不能为空")
    private Integer source;

}