package com.hongguoyan.module.biz.controller.admin.vipcouponlog.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 会员订阅变更流水新增/修改 Request VO")
@Data
public class VipCouponLogSaveReqVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "13137")
    private Long id;

    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "20486")
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @Schema(description = "套餐编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "套餐编码不能为空")
    private String planCode;

    @Schema(description = "动作：1开通,2续期,3撤销,4补偿", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "动作：1开通,2续期,3撤销,4补偿不能为空")
    private Integer action;

    @Schema(description = "来源：1支付,2券码,3后台", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "来源：1支付,2券码,3后台不能为空")
    private Integer source;

    @Schema(description = "关联类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "关联类型不能为空")
    private Integer refType;

    @Schema(description = "关联ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "8454")
    @NotEmpty(message = "关联ID不能为空")
    private String refId;

    @Schema(description = "变更前到期时间")
    private LocalDateTime beforeEndTime;

    @Schema(description = "变更后到期时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "变更后到期时间不能为空")
    private LocalDateTime afterEndTime;

    @Schema(description = "增加天数", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "增加天数不能为空")
    private Integer grantDays;

    @Schema(description = "备注", example = "随便")
    private String remark;

}