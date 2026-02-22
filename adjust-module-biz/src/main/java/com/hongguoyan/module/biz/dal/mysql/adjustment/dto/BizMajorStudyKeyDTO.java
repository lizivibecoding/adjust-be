package com.hongguoyan.module.biz.dal.mysql.adjustment.dto;

import lombok.Data;

/**
 * 复合键：学校/学院/专业/学习方式/年份
 */
@Data
public class BizMajorStudyKeyDTO {

    private Long schoolId;
    private Long collegeId;
    private Long majorId;
    private Integer studyMode;
    private Integer year;

}

