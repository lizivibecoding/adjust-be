package com.hongguoyan.module.biz.controller.app.useradjustment.vo;

import com.hongguoyan.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "发布调剂-公开分页-请求")
@Data
public class AppUserAdjustmentPublicPageReqVO extends PageParam {

    @Schema(description = "搜索关键词", example = "北京大学")
    private String keyword;
}

