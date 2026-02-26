package com.hongguoyan.module.biz.controller.admin.adjustment.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Data;

@Schema(description = "管理后台 - 调剂录取名单成绩修改 Request VO")
@Data
public class AdjustmentAdmitScoreUpdateReqVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "id 不能为空")
    private Long id;

    @Schema(description = "初试成绩", example = "385")
    private BigDecimal firstScore;

    @Schema(description = "复试成绩", example = "88")
    private BigDecimal retestScore;

    @Schema(description = "总成绩", example = "473")
    private BigDecimal totalScore;

    @AssertTrue(message = "至少修改一项成绩")
    public boolean isAnyScorePresent() {
        return firstScore != null || retestScore != null || totalScore != null;
    }
}

