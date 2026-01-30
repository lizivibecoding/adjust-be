package com.hongguoyan.module.biz.controller.app.useradjustment.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import cn.idev.excel.annotation.*;

@Schema(description = "用户 APP - 用户发布调剂 Response VO")
@Data
@ExcelIgnoreUnannotated
public class AppUserAdjustmentRespVO {

    @Schema(description = "用户发布调剂ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1558")
    @ExcelProperty("用户发布调剂ID")
    private Long id;

    @Schema(description = "发布人用户ID(member.user.id)", requiredMode = Schema.RequiredMode.REQUIRED, example = "16745")
    @ExcelProperty("发布人用户ID(member.user.id)")
    private Long userId;

    @Schema(description = "调剂信息标题", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("调剂信息标题")
    private String title;

    @Schema(description = "调剂年份", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("调剂年份")
    private Integer year;

    @Schema(description = "学校ID(biz_school.id)", requiredMode = Schema.RequiredMode.REQUIRED, example = "28868")
    @ExcelProperty("学校ID(biz_school.id)")
    private Long schoolId;

    @Schema(description = "学校名称(冗余)", requiredMode = Schema.RequiredMode.REQUIRED, example = "李四")
    @ExcelProperty("学校名称(冗余)")
    private String schoolName;

    @Schema(description = "学院ID(biz_school_college.id)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2253")
    @ExcelProperty("学院ID(biz_school_college.id)")
    private Long collegeId;

    @Schema(description = "学院名称(冗余)", requiredMode = Schema.RequiredMode.REQUIRED, example = "王五")
    @ExcelProperty("学院名称(冗余)")
    private String collegeName;

    @Schema(description = "专业ID(biz_major.id/按业务口径)", requiredMode = Schema.RequiredMode.REQUIRED, example = "11212")
    @ExcelProperty("专业ID(biz_major.id/按业务口径)")
    private Long majorId;

    @Schema(description = "专业代码", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("专业代码")
    private String majorCode;

    @Schema(description = "专业名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "芋艿")
    @ExcelProperty("专业名称")
    private String majorName;

    @Schema(description = "学位类型(0未知/不区分 1专硕 2学硕)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty("学位类型(0未知/不区分 1专硕 2学硕)")
    private Integer degreeType;

    @Schema(description = "方向ID(biz_school_direction.id)", requiredMode = Schema.RequiredMode.REQUIRED, example = "28566")
    @ExcelProperty("方向ID(biz_school_direction.id)")
    private Long directionId;

    @Schema(description = "方向代码", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("方向代码")
    private String directionCode;

    @Schema(description = "方向名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "芋艿")
    @ExcelProperty("方向名称")
    private String directionName;

    @Schema(description = "学习方式(1全日制 2非全日制)", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("学习方式(1全日制 2非全日制)")
    private Integer studyMode;

    @Schema(description = "调剂缺额人数", requiredMode = Schema.RequiredMode.REQUIRED, example = "19021")
    @ExcelProperty("调剂缺额人数")
    private Integer adjustCount;

    @Schema(description = "原计划招生人数/剩余(参考)", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("原计划招生人数/剩余(参考)")
    private Integer adjustLeft;

    @Schema(description = "联系方式(不区分手机/微信)", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("联系方式(不区分手机/微信)")
    private String contact;

    @Schema(description = "备注", requiredMode = Schema.RequiredMode.REQUIRED, example = "你说的对")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "状态(1开放 0关闭)", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("状态(1开放 0关闭)")
    private Integer status;

    @Schema(description = "发布时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("发布时间")
    private LocalDateTime publishTime;

    @Schema(description = "浏览次数", requiredMode = Schema.RequiredMode.REQUIRED, example = "19989")
    @ExcelProperty("浏览次数")
    private Integer viewCount;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}