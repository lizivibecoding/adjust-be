package com.hongguoyan.module.biz.dal.dataobject.schoolmajor;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import com.hongguoyan.framework.mybatis.core.dataobject.BaseDO;

/**
 * 院校专业 DO
 *
 * @author 芋道源码
 */
@TableName("biz_school_major")
@KeySequence("biz_school_major_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SchoolMajorDO extends BaseDO {

    /**
     * 主键
     */
    @TableId
    private Long id;
    /**
     * 学校ID(biz_school.id)
     */
    private Long schoolId;
    /**
     * 院系ID(biz_school_college.id)
     */
    private Long collegeId;
    /**
     * 专业ID(biz_special.id, level=3)
     */
    private Long majorId;
    /**
     * 专业代码(来源enroll 专业列括号内)
     */
    private String code;
    /**
     * 专业名称
     */
    private String name;
    /**
     * 学位类型(0=未知/不区分,1=专硕,2=学硕)
     */
    private Integer degreeType;
    /**
     * 热度值
     */
    private Integer viewCount;


}