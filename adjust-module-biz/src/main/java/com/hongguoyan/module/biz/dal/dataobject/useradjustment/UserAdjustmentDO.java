package com.hongguoyan.module.biz.dal.dataobject.useradjustment;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import com.hongguoyan.framework.mybatis.core.dataobject.BaseDO;

/**
 * 用户发布调剂 DO
 *
 * @author hgy
 */
@TableName("biz_user_adjustment")
@KeySequence("biz_user_adjustment_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAdjustmentDO extends BaseDO {

    /**
     * 用户发布调剂ID
     */
    @TableId
    private Long id;
    /**
     * 发布人用户ID(member.user.id)
     */
    private Long userId;
    /**
     * 调剂信息标题
     */
    private String title;
    /**
     * 调剂年份
     */
    private Integer year;
    /**
     * 学校ID(biz_school.id)
     */
    private Long schoolId;
    /**
     * 学校名称(冗余)
     */
    private String schoolName;
    /**
     * 学院ID(biz_school_college.id)
     */
    private Long collegeId;
    /**
     * 学院名称(冗余)
     */
    private String collegeName;
    /**
     * 专业ID(biz_major.id/按业务口径)
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
     * 学位类型(0未知/不区分 1专硕 2学硕)
     */
    private Integer degreeType;
    /**
     * 方向ID(biz_school_direction.id)
     */
    private Long directionId;
    /**
     * 方向代码
     */
    private String directionCode;
    /**
     * 方向名称
     */
    private String directionName;
    /**
     * 学习方式(1全日制 2非全日制)
     */
    private Integer studyMode;
    /**
     * 调剂缺额人数
     */
    private Integer adjustCount;
    /**
     * 原计划招生人数/剩余(参考)
     */
    private Integer adjustLeft;
    /**
     * 联系方式(不区分手机/微信)
     */
    private String contact;
    /**
     * 备注
     */
    private String remark;
    /**
     * 状态(1开放 0关闭)
     */
    private Integer status;
    /**
     * 发布时间
     */
    private LocalDateTime publishTime;
    /**
     * 浏览次数
     */
    private Integer viewCount;


}