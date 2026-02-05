package com.hongguoyan.module.biz.controller.admin.vipplanbenefit.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "管理后台 - 套餐权益（benefit）新增/修改 Request VO")
@Data
public class VipPlanBenefitSaveReqVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "25561")
    private Long id;

    @Schema(description = "套餐编码：VIP / SVIP", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "套餐编码不能为空")
    private String planCode;

    @Schema(description = "权益 key", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "权益 key 不能为空")
    private String benefitKey;

    @Schema(description = "权益名称（展示）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "权益名称不能为空")
    private String benefitName;

    @Schema(description = "权益描述（展示）")
    private String benefitDesc;

    @Schema(description = "权益类型：1=BOOLEAN 2=QUOTA(次数) 3=LIMIT(阈值)", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "权益类型不能为空")
    private Integer benefitType;

    @Schema(description = "数值（如 1/3/8；-1=不限）", example = "3")
    private Integer benefitValue;

    @Schema(description = "周期：0=NONE", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    @NotNull(message = "周期不能为空")
    private Integer periodType;

    @Schema(description = "计次策略：1=COUNT(每次计) 2=UNIQUE_KEY(按唯一键去重)", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "计次策略不能为空")
    private Integer consumePolicy;

    @Schema(description = "排序", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "排序不能为空")
    private Integer sort;

    @Schema(description = "展示开关：0 隐藏，1 展示", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "展示开关不能为空")
    private Integer displayStatus;

    @Schema(description = "功能开关：0 关闭，1 开启", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "功能开关不能为空")
    private Integer enabled;

}

