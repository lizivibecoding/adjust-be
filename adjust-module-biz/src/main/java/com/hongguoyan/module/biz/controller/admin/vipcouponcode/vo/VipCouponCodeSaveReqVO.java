package com.hongguoyan.module.biz.controller.admin.vipcouponcode.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 会员券码新增/修改 Request VO")
@Data
public class VipCouponCodeSaveReqVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "31822")
    private Long id;

    @Schema(description = "券码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "券码不能为空")
    private String code;

    @Schema(description = "批次号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "批次号不能为空")
    private String batchNo;

    @Schema(description = "套餐编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "套餐编码不能为空")
    private String planCode;

    @Schema(description = "状态：1未使用,2已使用,3过期,4作废", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "状态：1未使用,2已使用,3过期,4作废不能为空")
    private Integer status;

    @Schema(description = "开始时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "开始时间不能为空")
    private LocalDateTime validStartTime;

    @Schema(description = "截止时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "截止时间不能为空")
    private LocalDateTime validEndTime;

    @Schema(description = "使用人ID", example = "27020")
    private Long userId;

    @Schema(description = "使用时间")
    private LocalDateTime usedTime;

    @Schema(description = "备注", example = "你说的对")
    private String remark;

}