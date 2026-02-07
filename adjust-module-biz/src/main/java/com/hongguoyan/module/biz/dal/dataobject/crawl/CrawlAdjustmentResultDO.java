package com.hongguoyan.module.biz.dal.dataobject.crawl;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hongguoyan.framework.mybatis.core.dataobject.BaseDO;
import lombok.*;

/**
 * 爬虫数据调剂专业信息 DO
 *
 * @author hgy
 */
@TableName("crawl_adjustment_results")
@KeySequence("crawl_adjustment_results_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrawlAdjustmentResultDO extends BaseDO {

    /**
     * ID
     */
    @TableId
    private Long id;

    /**
     * 任务ID
     */
    private Long taskId;

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
     * 专业代码
     */
    private String majorCode;

    /**
     * 专业名称
     */
    private String majorName;

    /**
     * 学位类型
     */
    private String degreeType;

    /**
     * 学习方式
     */
    private String learningMethod;

    /**
     * 计划数
     */
    private String plannedCount;

    /**
     * 专项计划
     */
    private String specProject;

    /**
     * 研究方向
     */
    private String direction;

    /**
     * 调剂要求
     */
    private String remark;

    /**
     * 原始数据
     */
    private String rawJson;

}
