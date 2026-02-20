package com.hongguoyan.module.biz.controller.app.recommend.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hongguoyan.module.biz.framework.jackson.StudyModeNameSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Schema(description = "智能推荐结果-响应")
@Data
public class AppRecommendSchoolRespVO {

    @Schema(description = "学校ID", example = "5")
    private Long schoolId;

    @Schema(description = "调剂ID", example = "1024")
    private Long adjustment_id;

    @Schema(description = "学校名称", example = "清华大学")
    private String schoolName;

    @Schema(description = "学校Logo")
    private String schoolLogo;

    @Schema(description = "省份名称", example = "北京")
    private String provinceName;


    @Schema(description = "专业ID", example = "2810")
    private Long majorId;

    @Schema(description = "专业代码", example = "081200")
    private String majorCode;

    @Schema(description = "专业名称", example = "计算机科学与技术")
    private String majorName;

    @Schema(description = "方向ID", example = "755")
    private Long directionId;

    @Schema(description = "方向代码", example = "01")
    private String directionCode;

    @Schema(description = "方向名称", example = "人工智能")
    private String directionName;

    @Schema(description = "学位类型（0-不区分 1-学硕 2-专硕）", example = "1")
    private Integer degreeType;

    @Schema(description = "匹配概率(0-1)", example = "0.85")
    private Double matchProbability;

    @Schema(description = "概率分档(冲刺/稳妥/保底)", example = "稳妥")
    private String probabilityLabel;

    @Schema(description = "难度标签(难/中/易)", example = "中")
    private String difficultyLabel;

    @Schema(description = "年份", example = "2025")
    private Integer year;

    @Schema(description = "学院名称", example = "计算机学院")
    private String collegeName;

    @Schema(description = "学院ID", example = "1")
    private Long collegeId;

    @Schema(description = "学习方式", example = "全日制", type = "string")
    @JsonSerialize(using = StudyModeNameSerializer.class)
    private Integer studyMode;

    @Schema(description = "调剂招生人数", example = "5")
    private Integer planCount;

    @Schema(description = "录取最低分", example = "300")
    private BigDecimal minScore;

    @Schema(description = "录取中位分", example = "320")
    private BigDecimal medianScore;

    @Schema(description = "录取最高分", example = "350")
    private BigDecimal maxScore;

    @Schema(description = "院校排名差距描述(较大/适中/较小)", example = "较大")
    private String rankingGapLabel;

    @Schema(description = "去年录取平均分", example = "350")
    private BigDecimal lastYearAvgScore;

}
