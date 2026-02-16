package com.hongguoyan.module.biz.dal.mysql.home.dto;

import lombok.Data;

/**
 * 首页统计 - 日期聚合行
 */
@Data
public class HomeDateCountDTO {

    /**
     * 日期：yyyy-MM-dd
     */
    private String date;

    /**
     * 数量
     */
    private Long count;
}

