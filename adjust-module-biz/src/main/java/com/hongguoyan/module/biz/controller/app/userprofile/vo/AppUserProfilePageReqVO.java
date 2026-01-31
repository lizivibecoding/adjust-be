package com.hongguoyan.module.biz.controller.app.userprofile.vo;

import com.hongguoyan.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import static com.hongguoyan.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "用户 APP - 用户基础档案表(含成绩与软背景)分页 Request VO")
@Data
public class AppUserProfilePageReqVO extends PageParam {

    @Schema(description = "用户ID", example = "25443")
    private Long userId;

    @Schema(description = "本科毕业院校ID(biz_school.id)", example = "3320")
    private Long graduateSchoolId;

    @Schema(description = "本科毕业院校名称(冗余)", example = "李四")
    private String graduateSchoolName;

    @Schema(description = "本科专业ID(biz_major.id，可空)", example = "7856")
    private Long graduateMajorId;

    @Schema(description = "本科专业全称", example = "王五")
    private String graduateMajorName;

    @Schema(description = "本科专业大类(如电子信息类，辅助匹配)")
    private String graduateMajorClass;

    @Schema(description = "本科绩点 (GPA)")
    private BigDecimal undergraduateGpa;

    @Schema(description = "英语四级分数(空代表未填/未过)")
    private Integer cet4Score;

    @Schema(description = "英语六级分数(空代表未填/未过)")
    private Integer cet6Score;

    @Schema(description = "本科阶段获奖情况描述")
    private String undergraduateAwards;

    @Schema(description = "一志愿报考学校ID(biz_school.id)", example = "14765")
    private Long targetSchoolId;

    @Schema(description = "一志愿学校名称(冗余)", example = "王五")
    private String targetSchoolName;

    @Schema(description = "一志愿院系ID(biz_school_college.id)", example = "449")
    private Long targetCollegeId;

    @Schema(description = "一志愿院系名称(冗余)", example = "芋艿")
    private String targetCollegeName;

    @Schema(description = "一志愿专业ID(biz_school_major.id)", example = "8599")
    private Long targetMajorId;

    @Schema(description = "一志愿专业代码")
    private String targetMajorCode;

    @Schema(description = "一志愿专业名称(冗余)", example = "张三")
    private String targetMajorName;

    @Schema(description = "一志愿学位类型: 0-未知/不区分 1-专硕 2-学硕", example = "2")
    private Integer targetDegreeType;

    @Schema(description = "一志愿研究方向编码")
    private String targetDirectionCode;

    @Schema(description = "一志愿研究方向名称", example = "赵六")
    private String targetDirectionName;

    @Schema(description = "科目1代码")
    private String subjectCode1;

    @Schema(description = "科目1名称")
    private String subjectName1;

    @Schema(description = "科目1分数")
    private BigDecimal subjectScore1;

    @Schema(description = "科目2代码")
    private String subjectCode2;

    @Schema(description = "科目2名称")
    private String subjectName2;

    @Schema(description = "科目2分数")
    private BigDecimal subjectScore2;

    @Schema(description = "科目3代码")
    private String subjectCode3;

    @Schema(description = "科目3名称")
    private String subjectName3;

    @Schema(description = "科目3分数")
    private BigDecimal subjectScore3;

    @Schema(description = "科目4代码")
    private String subjectCode4;

    @Schema(description = "科目4名称")
    private String subjectName4;

    @Schema(description = "科目4分数")
    private BigDecimal subjectScore4;

    @Schema(description = "初试总分")
    private BigDecimal scoreTotal;

    @Schema(description = "自我介绍/个人优势")
    private String selfIntroduction;

    @Schema(description = "论文数量(一作/二作合计或按你们口径)", example = "870")
    private Integer paperCount;

    @Schema(description = "论文发表经历(描述型)")
    private String paperExperience;

    @Schema(description = "比赛经历ID列表(前端多选)")
    private String competitionIds;

    @Schema(description = "省二以上比赛次数(由多选统计)", example = "10458")
    private Integer competitionCount;

    @Schema(description = "比赛经历描述(可回填/拼接展示)")
    private String competitionExperience;

    @Schema(description = "软实力自评(0-10)")
    private Integer selfAssessedScore;

    @Schema(description = "基础信息是否锁定: 0-否 1-是")
    private Boolean basicLocked;

    @Schema(description = "提交时间(锁定时写入)")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] submitTime;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}

