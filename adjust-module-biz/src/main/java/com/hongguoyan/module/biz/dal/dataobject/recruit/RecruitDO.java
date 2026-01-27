package com.hongguoyan.module.biz.dal.dataobject.recruit;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import com.hongguoyan.framework.mybatis.core.dataobject.BaseDO;

/**
 * 招生 DO
 *
 * @author hgy
 */
@TableName("biz_recruit")
@KeySequence("biz_recruit_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecruitDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;
    /**
     * 招生年份
     */
    private Integer year;
    /**
     * 学校ID
     */
    private Long schoolId;
    /**
     * 学校名称
     */
    private String schoolName;
    /**
     * 学院ID
     */
    private Long collegeId;
    /**
     * 学院名称
     */
    private String collegeName;
    /**
     * 专业ID
     */
    private Long majorId;
    /**
     * 专业代码
     */
    private String majorCode;
    /**
     * 专业名称
     */
    private String majorName;
    /**
     * 学位类型(0=未知/不区分,1=专硕,2=学硕)
     */
    private Integer degreeType;
    /**
     * 方向代码(来自CSV方向列括号内)
     */
    private String directionCode;
    /**
     * 方向名称
     */
    private String directionName;
    /**
     * 学习方式(全日制/非全日制)
     */
    private String studyMode;
    /**
     * 考试方式
     */
    private String examMode;
    /**
     * major=专业人数,direction=方向人数
     */
    private String recruitType;
    /**
     * 拟招生人数
     */
    private Integer recruitNumber;
    /**
     * 人数描述
     */
    private String recruitDescription;
    /**
     * 指导老师
     */
    private String mentorName;
    /**
     * 退役计划
     */
    private Boolean retiredPlan;
    /**
     * 少骨计划
     */
    private Boolean shaoGuPlan;
    /**
     * 科目1代码
     */
    private String subjectCode1;
    /**
     * 科目1名称
     */
    private String subjectName1;
    /**
     * 科目1说明/参考书/链接(剥离自<>，不含<见招生简章>)
     */
    private String subjectNote1;
    /**
     * 科目2代码
     */
    private String subjectCode2;
    /**
     * 科目2名称
     */
    private String subjectName2;
    /**
     * 科目2说明/参考书/链接
     */
    private String subjectNote2;
    /**
     * 科目3代码
     */
    private String subjectCode3;
    /**
     * 科目3名称
     */
    private String subjectName3;
    /**
     * 科目3说明/参考书/链接
     */
    private String subjectNote3;
    /**
     * 科目4代码
     */
    private String subjectCode4;
    /**
     * 科目4名称
     */
    private String subjectName4;
    /**
     * 科目4说明/参考书/链接
     */
    private String subjectNote4;
    /**
     * 科目组合JSON
     */
    private String subjectCombinations;
    /**
     * 备注
     */
    private String remark;
    /**
     * 点击次数
     */
    private Long clicks;


}