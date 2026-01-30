package com.hongguoyan.module.biz.controller.app.useradjustment.vo;

import com.hongguoyan.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "用户 APP - 发布调剂公开分页 Request VO（仅搜索框）")
@Data
public class AppUserAdjustmentPublicPageReqVO extends PageParam {

    @Schema(description = "搜索关键词(学校/专业等)", example = "北京大学")
    private String keyword;
}

