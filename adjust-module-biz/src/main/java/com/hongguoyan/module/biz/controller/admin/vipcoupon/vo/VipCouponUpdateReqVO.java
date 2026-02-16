package com.hongguoyan.module.biz.controller.admin.vipcoupon.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hongguoyan.module.biz.framework.jackson.LocalDateTimeOrTimestampDeserializer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 会员券码更新 Request VO")
@Data
public class VipCouponUpdateReqVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "31822")
    @NotNull(message = "主键不能为空")
    private Long id;

    @Schema(description = "开始时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "开始时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeOrTimestampDeserializer.class)
    private LocalDateTime validStartTime;

    @Schema(description = "截止时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "截止时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeOrTimestampDeserializer.class)
    private LocalDateTime validEndTime;

    @Schema(description = "备注")
    private String remark;

    @AssertTrue(message = "有效期不合法")
    public boolean isValidTime() {
        if (validStartTime == null || validEndTime == null) {
            return false;
        }
        return validStartTime.isBefore(validEndTime);
    }

}

