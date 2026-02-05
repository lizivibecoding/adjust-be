package com.hongguoyan.module.biz.controller.app.adjustment.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = "调剂详情-响应")
@Data
public class AppAdjustmentDetailRespVO {

    // ========== 公共信息(同一 schoolId + majorId + collegeId + year + studyMode 下基本一致) ==========

    @Schema(description = "学校ID", example = "5")
    private Long schoolId;

    @Schema(description = "学校名称")
    private String schoolName;

    @Schema(description = "学校Logo")
    private String schoolLogo;

    @Schema(description = "省份名称")
    private String provinceName;

    @Schema(description = "学校类别 (综合类/理工类等)")
    private String schoolType;

    @Schema(description = "是否985")
    private Boolean is985;

    @Schema(description = "是否双一流")
    private Boolean isSyl;

    @Schema(description = "是否211")
    private Boolean is211;

    @Schema(description = "是否普通院校")
    private Boolean isOrdinary;

    @Schema(description = "学院ID", example = "95")
    private Long collegeId;

    @Schema(description = "学院名称")
    private String collegeName;

    @Schema(description = "专业ID", example = "2810")
    private Long majorId;

    @Schema(description = "专业代码")
    private String majorCode;

    @Schema(description = "专业名称")
    private String majorName;

    @Schema(description = "学位类型（0-不区分 1-学硕 2-专硕）")
    private Integer degreeType;

    @Schema(description = "调剂年份")
    private Integer year;

    @Schema(description = "学习方式：全日制/非全日制")
    private String studyMode;

    // ========== 方向列表(按 directionCode 从小到大排序) ==========

    @Schema(description = "方向详情列表")
    private List<AppAdjustmentDirectionDetailRespVO> directions;
}

