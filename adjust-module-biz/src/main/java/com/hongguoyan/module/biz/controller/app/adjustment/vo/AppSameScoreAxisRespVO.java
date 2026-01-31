package com.hongguoyan.module.biz.controller.app.adjustment.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "用户 APP - 同分调剂分数区间轴 Response VO")
@Data
public class AppSameScoreAxisRespVO {

    @Schema(description = "用户初试总分(向下取整)", example = "310")
    private Integer baseScore;

    @Schema(description = "可调整的最小分(固定为 baseScore-20，最小为0)", example = "290")
    private Integer minScore;

    @Schema(description = "可调整的最大分(固定为 baseScore+20)", example = "330")
    private Integer maxScore;

    @Schema(description = "默认开始分(初试)，初始化等于 minScore", example = "290")
    private Integer beginScore;

    @Schema(description = "默认结束分(初试)，初始化等于 maxScore", example = "330")
    private Integer endScore;
}

