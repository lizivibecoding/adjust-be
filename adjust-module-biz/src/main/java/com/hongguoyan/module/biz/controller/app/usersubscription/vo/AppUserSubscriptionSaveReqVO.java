package com.hongguoyan.module.biz.controller.app.usersubscription.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import jakarta.validation.constraints.*;

@Schema(description = "用户 APP - 用户调剂订阅新增/修改 Request VO")
@Data
public class AppUserSubscriptionSaveReqVO {

    @Schema(description = "订阅ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "11912")
    private Long id;

    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "6557")
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @Schema(description = "学校ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "11324")
    @NotNull(message = "学校ID不能为空")
    private Long schoolId;

    @Schema(description = "学院ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "16763")
    @NotNull(message = "学院ID不能为空")
    private Long collegeId;

    @Schema(description = "专业ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "7206")
    @NotNull(message = "专业ID不能为空")
    private Long majorId;

}