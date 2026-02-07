package com.hongguoyan.module.biz.dal.dataobject.crawl;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hongguoyan.framework.mybatis.core.dataobject.BaseDO;
import lombok.*;

/**
 * 爬虫数据调剂录取名单 DO
 *
 * @author hgy
 */
@TableName("crawl_admission_results")
@KeySequence("crawl_admission_results_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrawlAdmissionResultDO extends BaseDO {

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
     * 学习方式
     */
    private String learningMethod;

    /**
     * 考生姓名
     */
    private String candidateName;

    /**
     * 考生编号
     */
    private String candidateNo;

    /**
     * 初试总分
     */
    private String initialScore;

    /**
     * 一志愿报考学校
     */
    private String firstChoiceSchool;

    /**
     * 原始数据
     */
    private String rawJson;

}
