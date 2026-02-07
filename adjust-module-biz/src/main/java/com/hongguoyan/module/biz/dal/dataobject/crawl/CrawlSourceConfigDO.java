package com.hongguoyan.module.biz.dal.dataobject.crawl;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hongguoyan.framework.mybatis.core.dataobject.BaseDO;
import lombok.*;

/**
 * 公告页配置表 DO
 *
 * @author hgy
 */
@TableName("crawl_source_configs")
@KeySequence("crawl_source_configs_seq") // 兼容 Oracle、PostgreSQL 等
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrawlSourceConfigDO extends BaseDO {

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
     * 公告页地址
     */
    private String listUrl;

    /**
     * 是否启用
     */
    private Boolean isActive;

    /**
     * 定时表达式
     */
    private String cronExpression;

}
