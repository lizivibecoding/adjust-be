package com.hongguoyan.module.biz.controller.app.adjustment.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "用户 APP - 调剂搜索(院校 Tab) Response VO")
@Data
public class AppAdjustmentSearchSchoolRespVO {

    @Schema(description = "学校ID", example = "9101")
    private Long schoolId;

    @Schema(description = "学校名称")
    private String schoolName;

    @Schema(description = "学校类别", example = "综合类")
    private String schoolType;

    @Schema(description = "省份名称")
    private String provinceName;

    @Schema(description = "调剂年份")
    private Integer year;

    @JsonIgnore
    @Schema(hidden = true)
    private Long collegeId;

    @JsonIgnore
    @Schema(hidden = true)
    private Long majorId;

    @Schema(description = "统考招生人数")
    private Integer recruitNumber;

    @Schema(description = "调剂招生人数")
    private Integer adjustCount;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @Schema(description = "是否985")
    private Boolean is985;

    @Schema(description = "是否211")
    private Boolean is211;

    @Schema(description = "是否双一流")
    private Boolean isSyl;
}
