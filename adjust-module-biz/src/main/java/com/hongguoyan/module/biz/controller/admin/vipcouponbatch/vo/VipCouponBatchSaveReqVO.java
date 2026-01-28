package com.hongguoyan.module.biz.controller.admin.vipcouponbatch.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 会员券码批次新增/修改 Request VO")
@Data
public class VipCouponBatchSaveReqVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "9574")
    private Long id;

    @Schema(description = "批次号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "批次号不能为空")
    private String batchNo;

    @Schema(description = "套餐编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "套餐编码不能为空")
    private String planCode;

    @Schema(description = "开始时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "开始时间不能为空")
    private LocalDateTime validStartTime;

    @Schema(description = "截止时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "截止时间不能为空")
    private LocalDateTime validEndTime;

    @Schema(description = "总数", requiredMode = Schema.RequiredMode.REQUIRED, example = "26531")
    @NotNull(message = "总数不能为空")
    private Integer totalCount;

    @Schema(description = "已使用数", requiredMode = Schema.RequiredMode.REQUIRED, example = "11422")
    @NotNull(message = "已使用数不能为空")
    private Integer usedCount;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "状态不能为空")
    private Integer status;

    @Schema(description = "备注", example = "你猜")
    private String remark;

}