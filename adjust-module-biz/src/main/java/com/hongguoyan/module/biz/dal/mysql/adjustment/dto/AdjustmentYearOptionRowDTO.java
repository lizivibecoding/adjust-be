package com.hongguoyan.module.biz.dal.mysql.adjustment.dto;

import lombok.Data;

/**
 * 调剂详情切换选项（按年份聚合）行数据
 */
@Data
public class AdjustmentYearOptionRowDTO {

    /**
     * 年份
     */
    private Integer year;

    /**
     * 聚合后的来源优先级：1(研招网) < 2(院校官网) < 3(人工/第三方)
     */
    private Integer minPriority;

}

