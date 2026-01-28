package com.hongguoyan.module.biz.controller.admin.vipplanfeature.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import jakarta.validation.constraints.*;

@Schema(description = "管理后台 - 会员套餐权益新增/修改 Request VO")
@Data
public class VipPlanFeatureSaveReqVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "25561")
    private Long id;

    @Schema(description = "套餐编码：VIP / SVIP", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "套餐编码：VIP / SVIP不能为空")
    private String planCode;

    @Schema(description = "功能点 key", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "功能点 key不能为空")
    private String featureKey;

    @Schema(description = "权益名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @NotEmpty(message = "权益名称不能为空")
    private String name;

    @Schema(description = "权益描述", example = "你说的对")
    private String description;

    @Schema(description = "排序", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "排序不能为空")
    private Integer sort;

    @Schema(description = "展示开关：0 隐藏，1 展示", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "展示开关：0 隐藏，1 展示不能为空")
    private Integer status;

    @Schema(description = "功能开关：0 关闭，1 开启", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "功能开关：0 关闭，1 开启不能为空")
    private Integer enabled;

}