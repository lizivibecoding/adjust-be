package com.hongguoyan.module.biz.controller.app.useradjustment.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.hongguoyan.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static com.hongguoyan.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "用户发布调剂-分页-请求")
@Data
public class AppUserAdjustmentPageReqVO extends PageParam {

    @Schema(description = "发布人用户ID(member.user.id)", example = "16745")
    private Long userId;

    @Schema(description = "调剂信息标题")
    private String title;

    @Schema(description = "调剂年份")
    private Integer year;

    @Schema(description = "学校ID", example = "5")
    private Long schoolId;

    @Schema(description = "学校名称(冗余)", example = "李四")
    private String schoolName;

    @Schema(description = "学院ID", example = "95")
    private Long collegeId;

    @Schema(description = "学院名称(冗余)", example = "王五")
    private String collegeName;

    @Schema(description = "专业ID", example = "2810")
    private Long majorId;

    @Schema(description = "专业代码")
    private String majorCode;

    @Schema(description = "专业名称", example = "芋艿")
    private String majorName;

    @Schema(description = "学位类型（0-不区分 1-学硕 2-专硕）", example = "1")
    private Integer degreeType;

    @Schema(description = "方向ID", example = "755")
    private Long directionId;

    @Schema(description = "方向代码")
    private String directionCode;

    @Schema(description = "方向名称", example = "芋艿")
    private String directionName;

    @Schema(description = "学习方式：1-全日制 2-非全日制")
    private Integer studyMode;

    @Schema(description = "调剂缺额人数", example = "19021")
    private Integer adjustCount;

    @Schema(description = "原计划招生人数/剩余(参考)")
    private Integer adjustLeft;

    @Schema(description = "联系方式(不区分手机/微信)")
    private String contact;

    @Schema(description = "备注", example = "你说的对")
    private String remark;

    @Schema(description = "状态(1开放 0关闭)", example = "1")
    private Integer status;

    @Schema(description = "发布时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] publishTime;

    @Schema(description = "浏览次数", example = "19989")
    private Integer viewCount;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}