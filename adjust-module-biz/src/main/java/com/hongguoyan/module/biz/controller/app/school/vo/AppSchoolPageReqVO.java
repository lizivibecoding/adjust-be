package com.hongguoyan.module.biz.controller.app.school.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.hongguoyan.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static com.hongguoyan.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "用户 APP - 院校分页 Request VO")
@Data
public class AppSchoolPageReqVO extends PageParam {

    @Schema(description = "学校代码")
    private String schoolCode;

    @Schema(description = "学校名称", example = "李四")
    private String schoolName;

    @Schema(description = "学校Logo")
    private String schoolLogo;

    @Schema(description = "省份ID")
    private String provinceCode;

    @Schema(description = "省份名称", example = "李四")
    private String provinceName;

    @Schema(description = "考研分区：A区/B区")
    private String provinceArea;

    @Schema(description = "学校类别 (综合类/理工类等)", example = "2")
    private String schoolType;

    @Schema(description = "特性标签数组")
    private String feature;

    @Schema(description = "是否科研院所")
    private Boolean isAcademy;

    @Schema(description = "是否985")
    private Boolean is985;

    @Schema(description = "是否211")
    private Boolean is211;

    @Schema(description = "是否双一流")
    private Boolean isSyl;

    @Schema(description = "是否重点高校")
    private Boolean isKeySchool;

    @Schema(description = "是否普通高校")
    private Boolean isOrdinary;

    @Schema(description = "是否自主划线")
    private Boolean isZihuaxian;

    @Schema(description = "是否有推免资格")
    private Boolean isTuimian;

    @Schema(description = "是否可报考")
    private Boolean isApply;

    @Schema(description = "学校简介")
    private String intro;

    @Schema(description = "学校详细地址")
    private String schoolAddress;

    @Schema(description = "隶属部门")
    private String belongsTo;

    @Schema(description = "建校年份")
    private Integer createYear;

    @Schema(description = "占地面积(亩)")
    private Integer schoolSpace;

    @Schema(description = "官方网站数组")
    private String schoolSite;

    @Schema(description = "联系电话数组")
    private String schoolPhone;

    @Schema(description = "电子邮箱数组")
    private String schoolEmail;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}