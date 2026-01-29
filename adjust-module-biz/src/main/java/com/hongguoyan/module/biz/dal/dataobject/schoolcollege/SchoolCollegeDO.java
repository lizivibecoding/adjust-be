package com.hongguoyan.module.biz.dal.dataobject.schoolcollege;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import com.hongguoyan.framework.mybatis.core.dataobject.BaseDO;

/**
 * 学院 DO
 *
 * @author hgy
 */
@TableName("biz_school_college")
@KeySequence("biz_school_college_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SchoolCollegeDO extends BaseDO {

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
     * 院系代码
     */
    private String code;
    /**
     * 院系名称
     */
    private String name;


}