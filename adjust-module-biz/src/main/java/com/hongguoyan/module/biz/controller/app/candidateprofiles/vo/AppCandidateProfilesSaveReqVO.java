package com.hongguoyan.module.biz.controller.app.candidateprofiles.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;
import java.util.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import com.hongguoyan.module.biz.framework.jackson.LongListOrCommaSeparatedDeserializer;

@Schema(description = "用户 APP - 考生基础档案表(含成绩与软背景)新增/修改 Request VO")
@Data
public class AppCandidateProfilesSaveReqVO {

    @Schema(description = "本科毕业院校ID(biz_school.id)", requiredMode = Schema.RequiredMode.REQUIRED, example = "3320")
    @NotNull(message = "本科毕业院校ID(biz_school.id)不能为空")
    private Long graduateSchoolId;

    @Schema(description = "本科专业ID(biz_major.id)", requiredMode = Schema.RequiredMode.REQUIRED, example = "7856")
    @NotNull(message = "本科专业ID不能为空")
    private Long graduateMajorId;

    @Schema(description = "本科绩点 (GPA)")
    private BigDecimal undergraduateGpa;

    @Schema(description = "英语四级分数(空代表未填/未过)")
    private Integer cet4Score;

    @Schema(description = "英语六级分数(空代表未填/未过)")
    private Integer cet6Score;

    @Schema(description = "本科阶段获奖情况描述")
    private String undergraduateAwards;

    @Schema(description = "一志愿研究方向ID(biz_school_direction.id)", requiredMode = Schema.RequiredMode.REQUIRED, example = "20106")
    @NotNull(message = "一志愿研究方向ID不能为空")
    private Long targetDirectionId;

    @Schema(description = "科目1分数")
    private BigDecimal subjectScore1;

    @Schema(description = "科目2分数")
    private BigDecimal subjectScore2;

    @Schema(description = "科目3分数")
    private BigDecimal subjectScore3;

    @Schema(description = "科目4分数")
    private BigDecimal subjectScore4;

    @Schema(description = "初试总分")
    private BigDecimal scoreTotal;

    @Schema(description = "自我介绍/个人优势", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "自我介绍/个人优势不能为空")
    private String selfIntroduction;

    @Schema(description = "论文数量(一作/二作合计或按你们口径)", requiredMode = Schema.RequiredMode.REQUIRED, example = "870")
    @NotNull(message = "论文数量(一作/二作合计或按你们口径)不能为空")
    private Integer paperCount;

    @Schema(description = "比赛经历ID列表(前端多选)")
    @JsonDeserialize(using = LongListOrCommaSeparatedDeserializer.class)
    private List<Long> competitionIds;

    @Schema(description = "软实力自评(0-10)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "软实力自评(0-10)不能为空")
    private Integer selfAssessedScore;

}