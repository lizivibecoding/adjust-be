package com.hongguoyan.module.biz.controller.app.schooldirection.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import jakarta.validation.constraints.*;

@Schema(description = "院校研究方向-保存-请求")
@Data
public class AppSchoolDirectionSaveReqVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "755")
    private Long id;

    @Schema(description = "学校ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "5")
    @NotNull(message = "学校ID不能为空")
    private Long schoolId;

    @Schema(description = "学院ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "95")
    @NotNull(message = "学院ID不能为空")
    private Long collegeId;

    @Schema(description = "专业ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "2810")
    @NotNull(message = "专业ID不能为空")
    private Long majorId;

    @Schema(description = "学习方式", requiredMode = Schema.RequiredMode.REQUIRED, example = "全日制")
    @NotEmpty(message = "学习方式不能为空")
    private String studyMode;

    @Schema(description = "方向代码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "方向代码不能为空")
    private String directionCode;

    @Schema(description = "方向名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "李四")
    @NotEmpty(message = "方向名称不能为空")
    private String directionName;

}