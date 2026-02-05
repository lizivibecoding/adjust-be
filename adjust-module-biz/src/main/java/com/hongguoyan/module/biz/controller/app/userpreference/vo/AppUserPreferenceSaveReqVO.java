package com.hongguoyan.module.biz.controller.app.userpreference.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import jakarta.validation.constraints.*;

@Schema(description = "用户志愿-保存-请求")
@Data
public class AppUserPreferenceSaveReqVO {

    @Schema(description = "志愿序号:1一志愿 2二志愿 3三志愿", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "志愿序号不能为空")
    @Min(value = 1, message = "志愿序号必须在 1~3 之间")
    @Max(value = 3, message = "志愿序号必须在 1~3 之间")
    private Integer preferenceNo;

    @Schema(description = "最低至方向ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "755")
    @NotNull(message = "方向ID不能为空")
    private Long directionId;

}