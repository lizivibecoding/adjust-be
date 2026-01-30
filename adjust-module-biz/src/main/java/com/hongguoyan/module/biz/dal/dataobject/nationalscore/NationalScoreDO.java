package com.hongguoyan.module.biz.dal.dataobject.nationalscore;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import com.hongguoyan.framework.mybatis.core.dataobject.BaseDO;

/**
 * 国家线 DO
 *
 * @author hgy
 */
@TableName("biz_national_score")
@KeySequence("biz_national_score_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NationalScoreDO extends BaseDO {

    /**
     * 国家线ID
     */
    @TableId
    private Long id;
    /**
     * 年份
     */
    private Short year;
    /**
     * 学位类型(0=不区分,1=专硕,2=学硕)
     */
    private Integer degreeType;
    /**
     * 考研分区(A/B)
     */
    private String area;
    /**
     * 分数线类型(1=普通线,2=少数民族线)
     */
    private Integer scoreType;
    /**
     * 专业ID(biz_major.id)
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
     * 总分
     */
    private Short total;
    /**
     * 单科(满分=100)
     */
    @TableField("single_100")
    private Short single100;
    /**
     * 单科(满分=150)
     */
    @TableField("single_150")
    private Short single150;


}
