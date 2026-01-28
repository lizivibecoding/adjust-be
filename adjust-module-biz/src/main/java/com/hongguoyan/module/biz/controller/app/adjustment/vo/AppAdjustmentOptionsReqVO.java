package com.hongguoyan.module.biz.controller.app.adjustment.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "用户 APP - 调剂详情切换选项 Request VO")
@Data
public class AppAdjustmentOptionsReqVO {

    @Schema(description = "学校ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "9101")
    @NotNull(message = "学校ID不能为空")
    private Long schoolId;

    @Schema(description = "专业ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "15937")
    @NotNull(message = "专业ID不能为空")
    private Long majorId;

}

