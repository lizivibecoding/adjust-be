package com.hongguoyan.module.biz.controller.app.adjustment.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
import com.hongguoyan.module.biz.framework.jackson.StudyModeListMapNameSerializer;
import com.hongguoyan.module.biz.framework.jackson.StudyModeNameSerializer;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Schema(description = "学校联动选项-响应")
@Data
public class AppSchoolSpecialOptionsRespVO {

    @Schema(description = "类型(1=学院,2=专业+学习方式,3=研究方向,4=分数线)", example = "1")
    private Integer type;

    @Schema(description = "选项列表(type=1/2/3 返回)")
    private List<Option> options;

    @Schema(description = "方向列表(type=3 返回，每条包含 studyMode)")
    private List<DirectionOption> directions;

    @Schema(description = "学习方式映射(type=2 返回，key=majorId)")
    @JsonSerialize(using = StudyModeListMapNameSerializer.class)
    private Map<Long, List<Integer>> studyModes;

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
    public static class DirectionOption {

        @Schema(description = "方向ID", example = "755")
        private Long id;

        @Schema(description = "方向代码", example = "01")
        private String directionCode;

        @Schema(description = "方向名称", example = "人工智能")
        private String directionName;

        @Schema(description = "学习方式", example = "全日制", type = "string")
        @JsonSerialize(using = StudyModeNameSerializer.class)
        private Integer studyMode;
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

