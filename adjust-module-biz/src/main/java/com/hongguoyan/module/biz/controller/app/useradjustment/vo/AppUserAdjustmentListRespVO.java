package com.hongguoyan.module.biz.controller.app.useradjustment.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "用户 APP - 发布调剂列表 Item Response VO")
@Data
public class AppUserAdjustmentListRespVO {

    @Schema(description = "用户发布调剂ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1558")
    private Long id;

    @Schema(description = "调剂信息标题", requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;

    @Schema(description = "学校名称")
    private String schoolName;

    @Schema(description = "专业代码")
    private String majorCode;

    @Schema(description = "专业名称")
    private String majorName;

    @Schema(description = "发布时间")
    private LocalDateTime publishTime;

    @Schema(description = "招生人数展示文本(如：暂不确定/12)")
    private String quotaText;
}

