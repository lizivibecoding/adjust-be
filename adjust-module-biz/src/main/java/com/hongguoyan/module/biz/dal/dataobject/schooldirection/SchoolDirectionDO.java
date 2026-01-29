package com.hongguoyan.module.biz.dal.dataobject.schooldirection;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import com.hongguoyan.framework.mybatis.core.dataobject.BaseDO;

/**
 * 院校研究方向 DO
 *
 * @author hgy
 */
@TableName("biz_school_direction")
@KeySequence("biz_school_direction_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SchoolDirectionDO extends BaseDO {

    /**
     * ID
     */
    @TableId
    private Long id;
    /**
     * 学校ID
     */
    private Long schoolId;
    /**
     * 学院ID
     */
    private Long collegeId;
    /**
     * 专业ID
     */
    private Long majorId;
    /**
     * 学习方式
     */
    private String studyMode;
    /**
     * 方向代码
     */
    private String directionCode;
    /**
     * 方向名称
     */
    private String directionName;


}