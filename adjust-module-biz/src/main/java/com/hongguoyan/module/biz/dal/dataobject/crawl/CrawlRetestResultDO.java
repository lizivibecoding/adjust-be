package com.hongguoyan.module.biz.dal.dataobject.crawl;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hongguoyan.framework.mybatis.core.dataobject.BaseDO;
import lombok.*;

/**
 * 爬虫复试名单 DO
 *
 * @author hgy
 */
@TableName("crawl_retest_results")
@KeySequence("crawl_retest_results_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrawlRetestResultDO extends BaseDO {

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
     * 考生姓名
     */
    private String candidateName;

    /**
     * 考生编号
     */
    private String candidateNo;

    /**
     * 专业代码
     */
    private String majorCode;

    /**
     * 专业名称
     */
    private String majorName;

    /**
     * 政治分数
     */
    private String politicsScore;

    /**
     * 外语分数
     */
    private String foreignLanguageScore;

    /**
     * 业务课1分数
     */
    private String businessCourse1Score;

    /**
     * 业务课2分数
     */
    private String businessCourse2Score;

    /**
     * 总分
     */
    private String totalScore;

    /**
     * 备注
     */
    private String remark;

    /**
     * 原始数据
     */
    private String rawJson;

}
