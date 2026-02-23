package com.hongguoyan.module.biz.dal.mysql.adjustmentadmit.dto;

import lombok.Data;

/**
 * 同分去向-院校层次统计（聚合一次返回）。
 */
@Data
public class SameScoreLevelStatDTO {

    private Long c985;
    private Long c211;
    private Long csyl;
    private Long cother;

}

