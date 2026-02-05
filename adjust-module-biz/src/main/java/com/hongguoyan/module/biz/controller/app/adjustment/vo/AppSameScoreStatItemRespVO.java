package com.hongguoyan.module.biz.controller.app.adjustment.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "同分调剂去向院校层次统计-项-响应")
@Data
public class AppSameScoreStatItemRespVO {

    @Schema(description = "名称", example = "211工程")
    private String name;

    @Schema(description = "子名称", example = "不含985")
    private String subName;

    @Schema(description = "数量(学校数)", example = "5")
    private Long value;
}

