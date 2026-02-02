package com.hongguoyan.module.biz.dal.mysql.adjustment.dto;

import lombok.Data;

/**
 * Composite key for (school, college, major, year).
 */
@Data
public class BizMajorKeyDTO {

    private Long schoolId;
    private Long collegeId;
    private Long majorId;
    private Integer year;

}

