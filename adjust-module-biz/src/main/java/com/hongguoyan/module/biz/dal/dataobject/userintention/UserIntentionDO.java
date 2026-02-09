package com.hongguoyan.module.biz.dal.dataobject.userintention;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hongguoyan.framework.mybatis.core.dataobject.BaseDO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 用户调剂意向与偏好设置 DO
 *
 * @author hgy
 */
@TableName("biz_user_intention")
@KeySequence("biz_user_intention_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserIntentionDO extends BaseDO {

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
     * 意向省份 code 列表(biz_area.code)，JSON 存储
     */
    private String provinceCodes;
    /**
     * 屏蔽/一定不去的省份 code 列表(biz_area.code)，JSON 存储
     */
    private String excludeProvinceCodes;
    /**
     * 意向院校层次 code 列表（985/211/syl/ordinary），JSON 存储
     */
    private String schoolLevel;
    /**
     * 意向调剂专业/一级学科ID列表
     */
    private String majorIds;
    /**
     * 意向学习方式: 0-不限 1-全日制 2-非全日制
     */
    private Integer studyMode;
    /**
     * 意向学位类型: 0-不限 1-学硕 2-专硕
     */
    private Integer degreeType;
    /**
     * 是否包含专项计划: 0-否 1-是
     */
    private Boolean isSpecialPlan;
    /**
     * 是否接受科研院所: 0-否 1-是
     */
    private Boolean isAcceptResearchInst;
    /**
     * 是否接受跨专业调剂: 0-否 1-是
     */
    private Boolean isAcceptCrossMajor;
    /**
     * 是否接受跨考: 0-否 1-是
     */
    private Boolean isAcceptCrossExam;
    /**
     * 调剂优先级: 1-优先院校层次 2-优先专业匹配度
     */
    private Integer adjustPriority;

}

