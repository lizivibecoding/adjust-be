package com.hongguoyan.module.biz.controller.app.school.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "院校概况-响应")
@Data
public class AppSchoolOverviewRespVO {

    @Schema(description = "学校ID", example = "4667")
    private Long id;

    @Schema(description = "学校名称")
    private String schoolName;

    @Schema(description = "学校Logo")
    private String schoolLogo;

    @Schema(description = "省份名称")
    private String provinceName;

    @Schema(description = "考研分区：A区/B区")
    private String provinceArea;

    @Schema(description = "学校类别")
    private String schoolType;

    @Schema(description = "学校简介")
    private String intro;

    @Schema(description = "学校详细地址")
    private String schoolAddress;

    @Schema(description = "官方网站数组/字符串")
    private String schoolSite;

    @Schema(description = "联系电话数组/字符串")
    private String schoolPhone;

    @Schema(description = "电子邮箱数组/字符串")
    private String schoolEmail;

    @Schema(description = "是否普通高校")
    private Boolean isOrdinary;

    @Schema(description = "是否985")
    private Boolean is985;

    @Schema(description = "是否211")
    private Boolean is211;

    @Schema(description = "是否双一流")
    private Boolean isSyl;

}

