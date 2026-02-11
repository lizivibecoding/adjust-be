package com.hongguoyan.module.biz.controller.app.userprofile.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hongguoyan.module.biz.framework.jackson.LongListOrCommaSeparatedDeserializer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

@Schema(description = "用户基础档案-保存-请求")
@Data
public class AppUserProfileSaveReqVO {

    @Schema(description = "本科毕业院校ID(biz_school.id)", requiredMode = Schema.RequiredMode.REQUIRED, example = "3320")
    @NotNull(message = "本科毕业院校ID(biz_school.id)不能为空")
    private Long graduateSchoolId;

    @Schema(description = "本科专业ID(biz_major.id)", requiredMode = Schema.RequiredMode.REQUIRED, example = "7856")
    @NotNull(message = "本科专业ID不能为空")
    private Long graduateMajorId;

    @Schema(description = "本科平均分")
    private BigDecimal graduateAverageScore;

    @Schema(description = "本科绩点 (GPA)")
    private BigDecimal undergraduateGpa;

    @Schema(description = "本科专业学科排名：A+/A/A-/B+/B/B-/C+/C", example = "A")
    @Pattern(
            regexp = "^(A\\+|A|A-|B\\+|B|B-|C\\+|C)$",
            message = "本科专业学科排名仅支持 A+、A、A-、B+、B、B-、C+、C")
    private String graduateMajorRank;

    @Schema(description = "英语四级分数(空代表未填/未过)")
    private Integer cet4Score;

    @Schema(description = "英语六级分数(空代表未填/未过)")
    private Integer cet6Score;

    @Schema(description = "本科阶段获奖情况描述")
    private String undergraduateAwards;

    @Schema(description = "一志愿研究方向ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "755")
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

    @Schema(description = "论文数量(一作/二作合计或按你们口径)", example = "870")
    private Integer paperCount;

    @Schema(description = "比赛经历ID列表(前端多选)")
    @JsonDeserialize(using = LongListOrCommaSeparatedDeserializer.class)
    private List<Long> competitionIds;

    @Schema(description = "省二以上比赛次数(仅传数量时使用)", example = "2")
    private Integer competitionCount;

    @Schema(description = "本科获奖次数", example = "3")
    private Integer awardCount;

    @Schema(description = "是否获得国家级奖学金: 0-否 1-是")
    private Boolean isNationalScholarship;

    @Schema(description = "是否获得学校级及以上奖学金: 0-否 1-是")
    private Boolean isSchoolScholarship;

    @Schema(description = "软实力自评(0-10)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "软实力自评(0-10)不能为空")
    private Integer selfAssessedScore;

}

