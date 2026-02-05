package com.hongguoyan.module.biz.controller.app.adjustment.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Schema(description = "调剂详情切换选项-请求")
@Data
public class AppAdjustmentOptionsReqVO {

    @Schema(description = "学校ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "5")
    @NotNull(message = "学校ID不能为空")
    private Long schoolId;

    @Schema(description = "学院ID", example = "95")
    private Long collegeId;

    @Schema(description = "学习方式：全日制/非全日制", example = "全日制")
    @Size(max = 16, message = "学习方式长度不能超过16")
    private String studyMode;

    @Schema(description = "专业ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "2810")
    @NotNull(message = "专业ID不能为空")
    private Long majorId;

}

