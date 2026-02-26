package com.hongguoyan.module.biz.controller.admin.useradjustment.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 用户发布调剂（硕士招生）详情 Response VO")
@Data
public class UserAdjustmentAdminRespVO {

    @Schema(description = "ID", example = "1")
    private Long id;

    @Schema(description = "发布人用户ID(运营新增时：后台账号ID或指定发布者ID)")
    private Long userId;

    @Schema(description = "发布来源(1老师 2学长 3小道消息)")
    private Integer sourceType;

    @Schema(description = "年份")
    private Integer year;

    @Schema(description = "学校ID")
    private Long schoolId;

    @Schema(description = "学校名称")
    private String schoolName;

    @Schema(description = "学院ID")
    private Long collegeId;

    @Schema(description = "学院名称")
    private String collegeName;

    @Schema(description = "专业ID")
    private Long majorId;

    @Schema(description = "专业代码")
    private String majorCode;

    @Schema(description = "专业名称")
    private String majorName;

    @Schema(description = "方向ID")
    private Long directionId;

    @Schema(description = "方向名称")
    private String directionName;

    @Schema(description = "学习方式(1全日制 2非全日制)")
    private Integer studyMode;

    @Schema(description = "缺额人数")
    private Integer adjustCount;

    @Schema(description = "剩余名额/参考")
    private Integer adjustLeft;

    @Schema(description = "联系方式")
    private String contact;

    @Schema(description = "来源URL/原文链接")
    private String sourceUrl;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "状态(1开放 0关闭)")
    private Integer status;

    @Schema(description = "发布时间")
    private LocalDateTime publishTime;
}

