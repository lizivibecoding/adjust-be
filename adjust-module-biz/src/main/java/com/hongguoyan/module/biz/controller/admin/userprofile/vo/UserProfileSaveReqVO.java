package com.hongguoyan.module.biz.controller.admin.userprofile.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Schema(description = "管理后台 - 用户基础档案创建/修改 Request VO")
@Data
public class UserProfileSaveReqVO {

    @Schema(description = "ID", example = "1")
    private Long id;

    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @Schema(description = "本科毕业院校ID")
    private Long graduateSchoolId;
    @Schema(description = "本科毕业院校名称")
    private String graduateSchoolName;

    @Schema(description = "本科专业ID")
    private Long graduateMajorId;
    @Schema(description = "本科专业名称")
    private String graduateMajorName;

    @Schema(description = "本科平均分")
    private BigDecimal graduateAverageScore;
    @Schema(description = "本科绩点 (GPA)")
    private BigDecimal undergraduateGpa;

    @Schema(description = "英语四级分数")
    private Integer cet4Score;
    @Schema(description = "英语六级分数")
    private Integer cet6Score;

    @Schema(description = "本科阶段获奖情况描述")
    private String undergraduateAwards;

    @Schema(description = "一志愿学校ID")
    private Long targetSchoolId;
    @Schema(description = "一志愿学校名称")
    private String targetSchoolName;

    @Schema(description = "一志愿学院ID")
    private Long targetCollegeId;
    @Schema(description = "一志愿学院名称")
    private String targetCollegeName;

    @Schema(description = "一志愿专业ID")
    private Long targetMajorId;
    @Schema(description = "一志愿专业代码")
    private String targetMajorCode;
    @Schema(description = "一志愿专业名称")
    private String targetMajorName;
    @Schema(description = "一志愿学位类型(1=学硕, 2=专硕)")
    private Integer targetDegreeType;

    @Schema(description = "一志愿研究方向ID")
    private Long targetDirectionId;
    @Schema(description = "一志愿研究方向代码")
    private String targetDirectionCode;
    @Schema(description = "一志愿研究方向名称")
    private String targetDirectionName;

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

    @Schema(description = "自我介绍/个人优势")
    private String selfIntroduction;

    @Schema(description = "论文数量")
    private Integer paperCount;

    @Schema(description = "比赛经历ID列表")
    private String competitionIds; // Admin uses String for JSON or comma

    @Schema(description = "省二以上比赛次数")
    private Integer competitionCount;

    @Schema(description = "本科获奖次数")
    private Integer awardCount;

    @Schema(description = "是否获得国家级奖学金")
    private Boolean isNationalScholarship;
    @Schema(description = "是否获得学校级及以上奖学金")
    private Boolean isSchoolScholarship;

    @Schema(description = "软实力自评(0-10)")
    private Integer selfAssessedScore;

}
