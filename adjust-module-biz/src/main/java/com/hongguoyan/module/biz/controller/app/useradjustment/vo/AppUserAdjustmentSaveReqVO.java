package com.hongguoyan.module.biz.controller.app.useradjustment.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "用户 APP - 用户发布调剂新增/修改 Request VO")
@Data
public class AppUserAdjustmentSaveReqVO {

    @Schema(description = "用户发布调剂ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1558")
    private Long id;

    @Schema(description = "发布人用户ID(member.user.id)", requiredMode = Schema.RequiredMode.REQUIRED, example = "16745")
    @NotNull(message = "发布人用户ID(member.user.id)不能为空")
    private Long userId;

    @Schema(description = "调剂信息标题", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "调剂信息标题不能为空")
    private String title;

    @Schema(description = "调剂年份", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "调剂年份不能为空")
    private Integer year;

    @Schema(description = "学校ID(biz_school.id)", requiredMode = Schema.RequiredMode.REQUIRED, example = "28868")
    @NotNull(message = "学校ID(biz_school.id)不能为空")
    private Long schoolId;

    @Schema(description = "学校名称(冗余)", requiredMode = Schema.RequiredMode.REQUIRED, example = "李四")
    @NotEmpty(message = "学校名称(冗余)不能为空")
    private String schoolName;

    @Schema(description = "学院ID(biz_school_college.id)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2253")
    @NotNull(message = "学院ID(biz_school_college.id)不能为空")
    private Long collegeId;

    @Schema(description = "学院名称(冗余)", requiredMode = Schema.RequiredMode.REQUIRED, example = "王五")
    @NotEmpty(message = "学院名称(冗余)不能为空")
    private String collegeName;

    @Schema(description = "专业ID(biz_major.id/按业务口径)", requiredMode = Schema.RequiredMode.REQUIRED, example = "11212")
    @NotNull(message = "专业ID(biz_major.id/按业务口径)不能为空")
    private Long majorId;

    @Schema(description = "专业代码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "专业代码不能为空")
    private String majorCode;

    @Schema(description = "专业名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "芋艿")
    @NotEmpty(message = "专业名称不能为空")
    private String majorName;

    @Schema(description = "学位类型(0未知/不区分 1专硕 2学硕)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "学位类型(0未知/不区分 1专硕 2学硕)不能为空")
    private Integer degreeType;

    @Schema(description = "方向ID(biz_school_direction.id)", requiredMode = Schema.RequiredMode.REQUIRED, example = "28566")
    @NotNull(message = "方向ID(biz_school_direction.id)不能为空")
    private Long directionId;

    @Schema(description = "方向代码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "方向代码不能为空")
    private String directionCode;

    @Schema(description = "方向名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "芋艿")
    @NotEmpty(message = "方向名称不能为空")
    private String directionName;

    @Schema(description = "学习方式(1全日制 2非全日制)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "学习方式(1全日制 2非全日制)不能为空")
    private Integer studyMode;

    @Schema(description = "调剂缺额人数", requiredMode = Schema.RequiredMode.REQUIRED, example = "19021")
    @NotNull(message = "调剂缺额人数不能为空")
    private Integer adjustCount;

    @Schema(description = "原计划招生人数/剩余(参考)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "原计划招生人数/剩余(参考)不能为空")
    private Integer adjustLeft;

    @Schema(description = "联系方式(不区分手机/微信)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "联系方式(不区分手机/微信)不能为空")
    private String contact;

    @Schema(description = "备注", requiredMode = Schema.RequiredMode.REQUIRED, example = "你说的对")
    @NotEmpty(message = "备注不能为空")
    private String remark;

    @Schema(description = "状态(1开放 0关闭)", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "状态(1开放 0关闭)不能为空")
    private Integer status;

    @Schema(description = "发布时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "发布时间不能为空")
    private LocalDateTime publishTime;

    @Schema(description = "浏览次数", requiredMode = Schema.RequiredMode.REQUIRED, example = "19989")
    @NotNull(message = "浏览次数不能为空")
    private Integer viewCount;

}