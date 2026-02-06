package com.hongguoyan.module.biz.controller.app.adjustmentadmit.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "调剂录取名单列表-请求")
@Data
public class AppAdjustmentAdmitListReqVO {

    @Schema(description = "学校ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "5")
    @NotNull(message = "学校ID不能为空")
    private Long schoolId;

    @Schema(description = "学院ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "95")
    @NotNull(message = "学院ID不能为空")
    private Long collegeId;

    @Schema(description = "专业ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "2810")
    @NotNull(message = "专业ID不能为空")
    private Long majorId;

    @Schema(description = "年份", requiredMode = Schema.RequiredMode.REQUIRED, example = "2025")
    @NotNull(message = "年份不能为空")
    private Short year;

    @Schema(description = "学习方式：1-全日制 2-非全日制", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "学习方式不能为空")
    private Integer studyMode;

    @Schema(description = "方向ID", example = "755")
    private Long directionId;
}

