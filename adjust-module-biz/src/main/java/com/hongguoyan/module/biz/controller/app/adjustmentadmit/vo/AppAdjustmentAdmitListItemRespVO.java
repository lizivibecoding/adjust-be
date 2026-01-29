package com.hongguoyan.module.biz.controller.app.adjustmentadmit.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Schema(description = "用户 APP - 调剂录取名单列表 Item Response VO")
@Data
public class AppAdjustmentAdmitListItemRespVO {

    @Schema(description = "考生名称(脱敏)", example = "程*")
    private String candidateName;

    @Schema(description = "一志愿学校名称", example = "贵州大学")
    private String firstSchoolName;

    @Schema(description = "初试成绩")
    private BigDecimal firstScore;

    @Schema(description = "复试成绩")
    private BigDecimal retestScore;

    @Schema(description = "总成绩")
    private BigDecimal totalScore;
}

