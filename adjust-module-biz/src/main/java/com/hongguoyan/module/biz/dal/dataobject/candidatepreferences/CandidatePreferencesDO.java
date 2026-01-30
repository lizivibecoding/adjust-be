package com.hongguoyan.module.biz.dal.dataobject.candidatepreferences;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import com.hongguoyan.framework.mybatis.core.dataobject.BaseDO;

/**
 * 考生调剂意向与偏好设置 DO
 *
 * @author hgy
 */
@TableName("biz_candidate_preferences")
@KeySequence("biz_candidate_preferences_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CandidatePreferencesDO extends BaseDO {

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
     * 意向院校层次
     */
    private Integer schoolLevel;
    /**
     * 意向调剂专业/一级学科ID列表
     */
    private String majorIds;
    /**
     * 意向学习方式: 0-不限 1-全日制 2-非全日制
     */
    private Integer studyMode;
    /**
     * 意向学位类型: 0-不限 1-专硕 2-学硕
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
     * 调剂优先级: 1-优先院校层次 2-优先专业匹配度
     */
    private Integer adjustPriority;


}