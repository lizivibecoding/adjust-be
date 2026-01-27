package com.hongguoyan.module.biz.dal.dataobject.school;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import com.hongguoyan.framework.mybatis.core.dataobject.BaseDO;

/**
 * 院校 DO
 *
 * @author hgy
 */
@TableName("biz_school")
@KeySequence("biz_school_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SchoolDO extends BaseDO {

    /**
     * 学校ID
     */
    @TableId
    private Long id;
    /**
     * 学校代码
     */
    private String schoolCode;
    /**
     * 学校名称
     */
    private String schoolName;
    /**
     * 学校Logo
     */
    private String schoolLogo;
    /**
     * 省份ID
     */
    private String provinceCode;
    /**
     * 省份名称
     */
    private String provinceName;
    /**
     * 考研分区：A区/B区
     */
    private String provinceArea;
    /**
     * 学校类别 (综合类/理工类等)
     */
    private String schoolType;
    /**
     * 特性标签数组
     */
    private String feature;
    /**
     * 是否科研院所
     */
    private Boolean isAcademy;
    /**
     * 是否985
     */
    private Boolean is_985;
    /**
     * 是否211
     */
    private Boolean is_211;
    /**
     * 是否双一流
     */
    private Boolean isSyl;
    /**
     * 是否重点高校
     */
    private Boolean isKeySchool;
    /**
     * 是否普通高校
     */
    private Boolean isOrdinary;
    /**
     * 是否自主划线
     */
    private Boolean isZihuaxian;
    /**
     * 是否有推免资格
     */
    private Boolean isTuimian;
    /**
     * 是否可报考
     */
    private Boolean isApply;
    /**
     * 学校简介
     */
    private String intro;
    /**
     * 学校详细地址
     */
    private String schoolAddress;
    /**
     * 隶属部门
     */
    private String belongsTo;
    /**
     * 建校年份
     */
    private Integer createYear;
    /**
     * 占地面积(亩)
     */
    private Integer schoolSpace;
    /**
     * 官方网站数组
     */
    private String schoolSite;
    /**
     * 联系电话数组
     */
    private String schoolPhone;
    /**
     * 电子邮箱数组
     */
    private String schoolEmail;


}