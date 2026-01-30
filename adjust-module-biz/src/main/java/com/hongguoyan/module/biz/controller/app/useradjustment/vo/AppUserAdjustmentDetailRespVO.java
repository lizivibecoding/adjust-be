package com.hongguoyan.module.biz.controller.app.useradjustment.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "用户 APP - 调剂详情 Response VO")
@Data
public class AppUserAdjustmentDetailRespVO {

    @Schema(description = "用户发布调剂ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1558")
    private Long id;

    @Schema(description = "调剂信息标题", requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;

    @Schema(description = "调剂年份")
    private Integer year;

    @Schema(description = "学校名称")
    private String schoolName;

    @Schema(description = "学院名称")
    private String collegeName;

    @Schema(description = "专业代码")
    private String majorCode;

    @Schema(description = "专业名称")
    private String majorName;

    @Schema(description = "方向名称")
    private String directionName;

    @Schema(description = "学习方式(1全日制 2非全日制)")
    private Integer studyMode;

    @Schema(description = "调剂缺额人数")
    private Integer adjustCount;

    @Schema(description = "原计划招生人数/剩余(参考)")
    private Integer adjustLeft;

    @Schema(description = "招生人数展示文本(如：暂不确定/12)")
    private String quotaText;

    @Schema(description = "联系方式(未申请时会脱敏)")
    private String contact;

    @Schema(description = "调剂招生要求/具体要求")
    private String remark;

    @Schema(description = "发布时间")
    private LocalDateTime publishTime;

    @Schema(description = "查看人数/浏览次数")
    private Integer viewCount;
}

