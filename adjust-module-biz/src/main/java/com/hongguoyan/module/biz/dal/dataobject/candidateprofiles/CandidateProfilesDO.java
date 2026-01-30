package com.hongguoyan.module.biz.dal.dataobject.candidateprofiles;

import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import com.hongguoyan.framework.mybatis.core.dataobject.BaseDO;

/**
 * 考生基础档案表(含成绩与软背景) DO
 *
 * @author hgy
 */
@TableName("biz_candidate_profiles")
@KeySequence("biz_candidate_profiles_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CandidateProfilesDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 本科毕业院校ID(biz_school.id)
     */
    private Long graduateSchoolId;
    /**
     * 本科毕业院校名称(冗余)
     */
    private String graduateSchoolName;
    /**
     * 本科专业ID(biz_major.id，可空)
     */
    private Long graduateMajorId;
    /**
     * 本科专业全称
     */
    private String graduateMajorName;
    /**
     * 本科专业大类(如电子信息类，辅助匹配)
     */
    private String graduateMajorClass;
    /**
     * 本科绩点 (GPA)
     */
    private BigDecimal undergraduateGpa;
    /**
     * 英语四级分数(空代表未填/未过)
     */
    private Integer cet4Score;
    /**
     * 英语六级分数(空代表未填/未过)
     */
    private Integer cet6Score;
    /**
     * 本科阶段获奖情况描述
     */
    private String undergraduateAwards;
    /**
     * 一志愿报考学校ID(biz_school.id)
     */
    private Long targetSchoolId;
    /**
     * 一志愿学校名称(冗余)
     */
    private String targetSchoolName;
    /**
     * 一志愿院系ID(biz_school_college.id)
     */
    private Long targetCollegeId;
    /**
     * 一志愿院系名称(冗余)
     */
    private String targetCollegeName;
    /**
     * 一志愿专业ID(biz_school_major.id)
     */
    private Long targetMajorId;
    /**
     * 一志愿专业代码
     */
    private String targetMajorCode;
    /**
     * 一志愿专业名称(冗余)
     */
    private String targetMajorName;
    /**
     * 一志愿学位类型: 0-未知/不区分 1-专硕 2-学硕
     */
    private Integer targetDegreeType;
    /**
     * 一志愿研究方向编码
     */
    private String targetDirectionCode;
    /**
     * 一志愿研究方向名称
     */
    private String targetDirectionName;
    /**
     * 一志愿研究方向ID(biz_school_direction.id)
     */
    @TableField("target_direction_id")
    private Long targetDirectionId;
    /**
     * 科目1代码
     */
    private String subjectCode1;
    /**
     * 科目1名称
     */
    private String subjectName1;
    /**
     * 科目1分数
     */
    private BigDecimal subjectScore1;
    /**
     * 科目2代码
     */
    private String subjectCode2;
    /**
     * 科目2名称
     */
    private String subjectName2;
    /**
     * 科目2分数
     */
    private BigDecimal subjectScore2;
    /**
     * 科目3代码
     */
    private String subjectCode3;
    /**
     * 科目3名称
     */
    private String subjectName3;
    /**
     * 科目3分数
     */
    private BigDecimal subjectScore3;
    /**
     * 科目4代码
     */
    private String subjectCode4;
    /**
     * 科目4名称
     */
    private String subjectName4;
    /**
     * 科目4分数
     */
    private BigDecimal subjectScore4;
    /**
     * 初试总分
     */
    private BigDecimal scoreTotal;
    /**
     * 自我介绍/个人优势
     */
    private String selfIntroduction;
    /**
     * 论文数量(一作/二作合计或按你们口径)
     */
    private Integer paperCount;
    /**
     * 论文发表经历(描述型)
     */
    private String paperExperience;
    /**
     * 比赛经历ID列表(前端多选)
     */
    private String competitionIds;
    /**
     * 省二以上比赛次数(由多选统计)
     */
    private Integer competitionCount;
    /**
     * 比赛经历描述(可回填/拼接展示)
     */
    private String competitionExperience;
    /**
     * 软实力自评(0-10)
     */
    private Integer selfAssessedScore;
    /**
     * 基础信息是否锁定: 0-否 1-是
     */
    private Boolean basicLocked;
    /**
     * 提交时间(锁定时写入)
     */
    private LocalDateTime submitTime;


}