package com.hongguoyan.module.biz.dal.dataobject.userpreference;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import com.hongguoyan.framework.mybatis.core.dataobject.BaseDO;

/**
 * 用户志愿 DO
 *
 * @author hgy
 */
@TableName("biz_user_preference")
@KeySequence("biz_user_preference_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPreferenceDO extends BaseDO {

    /**
     * 用户志愿ID
     */
    @TableId
    private Long id;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 志愿序号:1一志愿 2二志愿 3三志愿
     */
    private Integer preferenceNo;
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
     * 方向ID
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
     * 学习方式:1全日制 2非全日制
     */
    private Integer studyMode;


}