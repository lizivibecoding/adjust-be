package com.hongguoyan.module.biz.controller.app.adjustment.vo;

import com.hongguoyan.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "热门调剂专业排名-请求")
@Data
public class AppAdjustmentHotRankingReqVO extends PageParam {

    @Schema(description = "调剂年份", example = "2025")
    private Integer year;

    @Schema(description = "省份代码", example = "110000")
    private String provinceCode;

    @Schema(description = "院校层次(数值：1/2/3...)", example = "2")
    private Integer schoolLevel;

    @Schema(description = "学习方式：1-全日制 2-非全日制", example = "1")
    private Integer studyMode;
}

