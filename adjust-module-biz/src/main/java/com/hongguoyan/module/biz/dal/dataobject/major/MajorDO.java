package com.hongguoyan.module.biz.dal.dataobject.major;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import com.hongguoyan.framework.mybatis.core.dataobject.BaseDO;

/**
 * 专业 DO
 *
 * @author hgy
 */
@TableName("biz_major")
@KeySequence("biz_major_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MajorDO extends BaseDO {

    /**
     * 主键
     */
    @TableId
    private Long id;
    /**
     * 层级(1/2/3)
     */
    private Integer level;
    /**
     * 外部ID(cnf_spe_id/spe_id)
     */
    private Integer extId;
    /**
     * 父节点ID(指向biz_special.id)
     */
    private Long parentId;
    /**
     * 父级代码
     */
    private String parentCode;
    /**
     * 代码
     */
    private String code;
    /**
     * 名称
     */
    private String name;
    /**
     * 学位类型(0=两者/不适用,1=学硕,2=专硕)
     */
    private Integer degreeType;


}