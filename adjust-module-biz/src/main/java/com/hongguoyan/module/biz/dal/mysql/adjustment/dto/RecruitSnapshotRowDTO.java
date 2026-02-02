package com.hongguoyan.module.biz.dal.mysql.adjustment.dto;

import lombok.Data;

/**
 * Recruit snapshot row for a major key.
 */
@Data
public class RecruitSnapshotRowDTO {

    private Long schoolId;
    private Long collegeId;
    private Long majorId;
    private Integer year;
    private Integer recruitNumber;

}

