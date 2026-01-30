package com.hongguoyan.module.biz.dal.dataobject.useradjustmentapply;

import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import com.hongguoyan.framework.mybatis.core.dataobject.BaseDO;

/**
 * 用户发布调剂申请记录 DO
 *
 * @author hgy
 */
@TableName("biz_user_adjustment_apply")
@KeySequence("biz_user_adjustment_apply_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAdjustmentApplyDO extends BaseDO {

    /**
     * 用户发布调剂申请ID
     */
    @TableId
    private Long id;
    /**
     * 用户发布调剂ID(biz_user_adjustment.id)
     */
    private Long userAdjustmentId;
    /**
     * 申请人用户ID(member.user.id)
     */
    private Long userId;
    /**
     * 姓名
     */
    private String candidateName;
    /**
     * 联系方式(手机号/微信)
     */
    private String contact;
    /**
     * 一志愿报考学校ID(biz_school.id)
     */
    private Long firstSchoolId;
    /**
     * 一志愿报考学校名称(冗余)
     */
    private String firstSchoolName;
    /**
     * 一志愿报考专业ID(biz_major.id)
     */
    private Long firstMajorId;
    /**
     * 一志愿报考专业代码(冗余)
     */
    private String firstMajorCode;
    /**
     * 一志愿报考专业名称(冗余)
     */
    private String firstMajorName;
    /**
     * 第一门成绩
     */
    private BigDecimal subjectScore1;
    /**
     * 第二门成绩
     */
    private BigDecimal subjectScore2;
    /**
     * 第三门成绩
     */
    private BigDecimal subjectScore3;
    /**
     * 第四门成绩
     */
    private BigDecimal subjectScore4;
    /**
     * 总分
     */
    private BigDecimal totalScore;
    /**
     * 自我介绍&个人优势
     */
    private String note;


}