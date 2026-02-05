package com.hongguoyan.module.biz.controller.app.banner.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "轮播图-响应")
@Data
public class AppBannerRespVO {

    @Schema(description = "轮播图ID", example = "1024")
    private Long id;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "图片地址")
    private String picUrl;

    @Schema(description = "跳转类型(0=无,1=H5,2=小程序页面)", example = "2")
    private Integer linkType;

    @Schema(description = "跳转链接/小程序路径")
    private String linkUrl;

    @Schema(description = "按钮文案")
    private String ctaText;

}

