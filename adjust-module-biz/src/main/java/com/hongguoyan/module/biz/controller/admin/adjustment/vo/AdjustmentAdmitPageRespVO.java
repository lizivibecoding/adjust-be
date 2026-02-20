package com.hongguoyan.module.biz.controller.admin.adjustment.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Schema(description = "管理后台 - 调剂录取名单分页 Response VO")
@Data
public class AdjustmentAdmitPageRespVO {

    @Schema(description = "ID", example = "1")
    private Long id;

    @Schema(description = "考生姓名（脱敏）", example = "张*三")
    private String candidateName;

    @Schema(description = "一志愿学校", example = "清华大学")
    private String firstSchoolName;

    @Schema(description = "初试成绩", example = "385")
    private BigDecimal firstScore;

    @Schema(description = "复试成绩", example = "88")
    private BigDecimal retestScore;

    @Schema(description = "总成绩", example = "473")
    private BigDecimal totalScore;

}

