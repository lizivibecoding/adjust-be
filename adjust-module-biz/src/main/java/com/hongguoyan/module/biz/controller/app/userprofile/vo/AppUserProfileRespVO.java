package com.hongguoyan.module.biz.controller.app.userprofile.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

@Schema(description = "用户基础档案-响应")
@Data
@ExcelIgnoreUnannotated
public class AppUserProfileRespVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "10464")
    @ExcelProperty("主键ID")
    private Long id;

    @Schema(description = "本科毕业院校ID(biz_school.id)", requiredMode = Schema.RequiredMode.REQUIRED, example = "3320")
    @ExcelProperty("本科毕业院校ID(biz_school.id)")
    private Long graduateSchoolId;

    @Schema(description = "本科毕业院校名称(冗余)", requiredMode = Schema.RequiredMode.REQUIRED, example = "李四")
    @ExcelProperty("本科毕业院校名称(冗余)")
    private String graduateSchoolName;

    @Schema(description = "本科专业ID(biz_major.id，可空)", example = "7856")
    @ExcelProperty("本科专业ID(biz_major.id，可空)")
    private Long graduateMajorId;

    @Schema(description = "本科专业全称", requiredMode = Schema.RequiredMode.REQUIRED, example = "王五")
    @ExcelProperty("本科专业全称")
    private String graduateMajorName;

    @Schema(description = "本科专业大类(如电子信息类，辅助匹配)", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("本科专业大类(如电子信息类，辅助匹配)")
    private String graduateMajorClass;

    @Schema(description = "本科平均分")
    @ExcelProperty("本科平均分")
    private BigDecimal graduateAverageScore;

    @Schema(description = "本科绩点 (GPA)")
    @ExcelProperty("本科绩点 (GPA)")
    private BigDecimal undergraduateGpa;

    @Schema(description = "本科专业学科排名：A+/A/A-/B+/B/B-/C+/C")
    @ExcelProperty("本科专业学科排名")
    private String graduateMajorRank;

    @Schema(description = "英语四级分数(空代表未填/未过)")
    @ExcelProperty("英语四级分数(空代表未填/未过)")
    private Integer cet4Score;

    @Schema(description = "英语六级分数(空代表未填/未过)")
    @ExcelProperty("英语六级分数(空代表未填/未过)")
    private Integer cet6Score;

    @Schema(description = "本科阶段获奖情况描述")
    @ExcelProperty("本科阶段获奖情况描述")
    private String undergraduateAwards;

    @Schema(description = "本科获奖次数")
    @ExcelProperty("本科获奖次数")
    private Integer awardCount;

    @Schema(description = "是否获得国家级奖学金: 0-否 1-是")
    @ExcelProperty("是否获得国家级奖学金: 0-否 1-是")
    private Boolean isNationalScholarship;

    @Schema(description = "是否获得学校级及以上奖学金: 0-否 1-是")
    @ExcelProperty("是否获得学校级及以上奖学金: 0-否 1-是")
    private Boolean isSchoolScholarship;

    @Schema(description = "一志愿报考学校ID(biz_school.id)", example = "14765")
    @ExcelProperty("一志愿报考学校ID(biz_school.id)")
    private Long targetSchoolId;

    @Schema(description = "一志愿学校名称(冗余)", requiredMode = Schema.RequiredMode.REQUIRED, example = "王五")
    @ExcelProperty("一志愿学校名称(冗余)")
    private String targetSchoolName;

    @Schema(description = "一志愿院系ID(biz_school_college.id)", example = "449")
    @ExcelProperty("一志愿院系ID(biz_school_college.id)")
    private Long targetCollegeId;

    @Schema(description = "一志愿院系名称(冗余)", requiredMode = Schema.RequiredMode.REQUIRED, example = "芋艿")
    @ExcelProperty("一志愿院系名称(冗余)")
    private String targetCollegeName;

    @Schema(description = "一志愿专业ID(biz_school_major.id)", example = "8599")
    @ExcelProperty("一志愿专业ID(biz_school_major.id)")
    private Long targetMajorId;

    @Schema(description = "一志愿专业代码", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("一志愿专业代码")
    private String targetMajorCode;

    @Schema(description = "一志愿专业名称(冗余)", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @ExcelProperty("一志愿专业名称(冗余)")
    private String targetMajorName;

    @Schema(description = "一志愿学位类型：0-不区分 1-学硕 2-专硕", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("一志愿学位类型：0-不区分 1-学硕 2-专硕")
    private Integer targetDegreeType;

    @Schema(description = "一志愿研究方向编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("一志愿研究方向编码")
    private String targetDirectionCode;

    @Schema(description = "一志愿研究方向名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "赵六")
    @ExcelProperty("一志愿研究方向名称")
    private String targetDirectionName;

    @Schema(description = "科目1名称")
    @ExcelProperty("科目1名称")
    private String subjectName1;

    @Schema(description = "科目1代码")
    @ExcelProperty("科目1代码")
    private String subjectCode1;

    @Schema(description = "科目1分数")
    @ExcelProperty("科目1分数")
    private BigDecimal subjectScore1;

    @Schema(description = "科目2名称")
    @ExcelProperty("科目2名称")
    private String subjectName2;

    @Schema(description = "科目2代码")
    @ExcelProperty("科目2代码")
    private String subjectCode2;

    @Schema(description = "科目2分数")
    @ExcelProperty("科目2分数")
    private BigDecimal subjectScore2;

    @Schema(description = "科目3名称")
    @ExcelProperty("科目3名称")
    private String subjectName3;

    @Schema(description = "科目3代码")
    @ExcelProperty("科目3代码")
    private String subjectCode3;

    @Schema(description = "科目3分数")
    @ExcelProperty("科目3分数")
    private BigDecimal subjectScore3;

    @Schema(description = "科目4名称")
    @ExcelProperty("科目4名称")
    private String subjectName4;

    @Schema(description = "科目4代码")
    @ExcelProperty("科目4代码")
    private String subjectCode4;

    @Schema(description = "科目4分数")
    @ExcelProperty("科目4分数")
    private BigDecimal subjectScore4;

    @Schema(description = "初试总分")
    @ExcelProperty("初试总分")
    private BigDecimal scoreTotal;

    @Schema(description = "自我介绍/个人优势", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("自我介绍/个人优势")
    private String selfIntroduction;

    @Schema(description = "论文数量(一作/二作合计或按你们口径)", example = "870")
    @ExcelProperty("论文数量(一作/二作合计或按你们口径)")
    private Integer paperCount;

    @Schema(description = "比赛经历ID列表(前端多选)")
    @ExcelProperty("比赛经历ID列表(前端多选)")
    private String competitionIds;

    @Schema(description = "省二以上比赛次数(由多选统计)", example = "10458")
    @ExcelProperty("省二以上比赛次数(由多选统计)")
    private Integer competitionCount;

    @Schema(description = "软实力自评(0-10)", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("软实力自评(0-10)")
    private Integer selfAssessedScore;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "修改次数")
    private Integer editNum;

}

