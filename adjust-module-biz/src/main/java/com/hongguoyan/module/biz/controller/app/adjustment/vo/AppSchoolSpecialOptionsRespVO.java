package com.hongguoyan.module.biz.controller.app.adjustment.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Schema(description = "用户 APP - 学校联动选项 Response VO")
@Data
public class AppSchoolSpecialOptionsRespVO {

    @Schema(description = "类型(1=学院,2=专业+学习方式,3=研究方向,4=分数线)", example = "1")
    private Integer type;

    @Schema(description = "选项列表(type=1/2/3 返回)")
    private List<Option> options;

    @Schema(description = "学习方式映射(type=2 返回，key=majorId)")
    private Map<Long, List<String>> studyModeMap;

    @Schema(description = "分数线(type=4 返回)")
    private ScoreLimit scoreLimit;

    @Data
    public static class Option {

        @Schema(description = "ID", example = "11484")
        private Long id;

        @Schema(description = "代码(专业可用)", example = "085500")
        private String code;

        @Schema(description = "名称", example = "美术学院")
        private String name;
    }

    @Data
    public static class ScoreLimit {

        @Schema(description = "政治分数线", example = "100")
        private Integer politicsScoreLimit;

        @Schema(description = "外语分数线", example = "100")
        private Integer englishScoreLimit;

        @Schema(description = "业务课1分数线", example = "300")
        private Integer specialOneScoreLimit;

        @Schema(description = "业务课2分数线", example = "150")
        private Integer specialTwoScoreLimit;

        @Schema(description = "总分分数线", example = "500")
        private Integer totalScoreLimit;
    }
}

