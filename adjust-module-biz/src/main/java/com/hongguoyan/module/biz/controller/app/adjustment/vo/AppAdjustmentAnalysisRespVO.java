package com.hongguoyan.module.biz.controller.app.adjustment.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Schema(description = "用户 APP - 调剂分析 Response VO")
@Data
public class AppAdjustmentAnalysisRespVO {

    @Schema(description = "分数段分布")
    private List<NameValue> section;

    @Schema(description = "一志愿院校层次占比(按学校去重计数)")
    private List<LevelItem> level;

    @Schema(description = "录取名单数据概览(k1~k7)")
    private Map<String, DetailItem> detail;

    @Data
    public static class NameValue {
        @Schema(description = "名称", example = "315-320")
        private String name;
        @Schema(description = "数量", example = "1")
        private Long value;
    }

    @Data
    public static class LevelItem {
        @Schema(description = "名称", example = "211工程")
        private String name;
        @Schema(description = "子名称", example = "不含985")
        private String subName;
        @Schema(description = "数量(学校数)", example = "1")
        private Long value;
    }

    @Data
    public static class DetailItem {
        @Schema(description = "名称", example = "调剂最低分")
        private String name;
        @Schema(description = "值")
        private BigDecimal value;
    }
}

