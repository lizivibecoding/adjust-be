package com.hongguoyan.module.biz.controller.app.adjustment.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "调剂搜索-专业-响应")
@Data
public class AppAdjustmentSearchRespVO {

    @Schema(description = "学校ID", example = "5")
    private Long schoolId;

    @Schema(description = "学院ID", example = "95")
    private Long collegeId;

    @Schema(description = "专业ID", example = "2810")
    private Long majorId;

    @Schema(description = "学校名称")
    private String schoolName;

    @Schema(description = "学院名称")
    private String collegeName;

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

    @Schema(description = "统考招生人数")
    private Integer recruitNumber;

    @Schema(description = "调剂招生人数")
    private Integer adjustCount;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @Schema(description = "热度值(内部字段，不对外返回)", hidden = true)
    @JsonIgnore
    private Integer viewCount;

    @Schema(description = "热度分(内部字段，不对外返回)", hidden = true)
    @JsonIgnore
    private Long hotScore;

    @Schema(description = "热力值")
    private Integer heat;

    @Schema(description = "省份名称")
    private String provinceName;

    @Schema(description = "今年调剂概率大")
    private Boolean highAdjustChance;

}
