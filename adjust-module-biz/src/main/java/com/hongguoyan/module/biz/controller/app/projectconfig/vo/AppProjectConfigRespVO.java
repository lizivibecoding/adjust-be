package com.hongguoyan.module.biz.controller.app.projectconfig.vo;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Data;

@Schema(description = "项目配置-响应")
@Data
@JsonPropertyOrder({"statYear", "adjustmentYears", "sameScoreYears"})
public class AppProjectConfigRespVO {

    @Schema(description = "调剂统计默认年份(取 adjustmentYears 第一个)", example = "2026")
    private Integer statYear;

    @Schema(description = "调剂数据年份列表(倒序；无数据则返回 [activeYear])", example = "[2026,2025]")
    private List<Integer> adjustmentYears;

    @Schema(description = "同分调剂占用年份(取 adjustmentYears 中 <= activeYear-1 的年份；用于从 adjustmentYears 中剔除)",
            example = "[2025,2024]")
    private List<Integer> sameScoreYears;
}

