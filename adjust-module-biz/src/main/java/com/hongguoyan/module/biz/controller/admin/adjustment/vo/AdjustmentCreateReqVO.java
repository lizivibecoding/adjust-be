package com.hongguoyan.module.biz.controller.admin.adjustment.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "管理后台 - 调剂新增 Request VO（只传方向ID，其余回填）")
@Data
public class AdjustmentCreateReqVO {

    @Schema(description = "方向ID(biz_school_direction.id)", requiredMode = Schema.RequiredMode.REQUIRED, example = "755")
    @NotNull(message = "方向不能为空")
    private Long directionId;

    @Schema(description = "调剂人数", requiredMode = Schema.RequiredMode.REQUIRED, example = "10")
    @NotNull(message = "调剂人数不能为空")
    @Min(value = 0, message = "调剂人数不能小于 0")
    private Integer adjustCount;

    @Schema(description = "来源URL/原文链接", example = "https://yz.chsi.com.cn/xxx")
    private String sourceUrl;

    @Schema(description = "备注")
    private String remark;
}

