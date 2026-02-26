package com.hongguoyan.module.biz.controller.admin.useradjustment.vo;

import com.hongguoyan.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 用户发布调剂（硕士招生）分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UserAdjustmentAdminPageReqVO extends PageParam {

    @Schema(description = "关键词（学校/学院/专业/方向/标题）")
    private String keyword;

    @Schema(description = "年份", example = "2026")
    private Integer year;

    @Schema(description = "发布来源(1老师 2学长 3小道消息)", example = "3")
    private Integer sourceType;
}

