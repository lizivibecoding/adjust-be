package com.hongguoyan.module.biz.controller.app.adjustment.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Schema(description = "用户 APP - 调剂分析 Response VO")
@Data
public class AppAdjustmentAnalysisRespVO {

    @Schema(description = "分数段分布")
    private List<NameValue> section;

    @Schema(description = "一志愿院校层次占比(按学校去重计数)")
    private List<LevelItem> level;

    @Schema(description = "录取名单数据概览")
    private Detail detail;

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
    public static class Detail {

        @Schema(description = "一志愿拟录取人数", example = "28")
        private Long firstChoiceCount;

        @Schema(description = "一志愿最高分", example = "356")
        private BigDecimal firstChoiceMaxScore;

        @Schema(description = "一志愿最低分", example = "267")
        private BigDecimal firstChoiceMinScore;

        @Schema(description = "拟招收调剂人数", example = "4")
        private Long adjustCount;

        @Schema(description = "调剂最低分", example = "316")
        private BigDecimal adjustMinScore;

        @Schema(description = "调剂中位分", example = "324.5")
        private BigDecimal adjustMedianScore;

        @Schema(description = "调剂最高分", example = "335")
        private BigDecimal adjustMaxScore;
    }
}

