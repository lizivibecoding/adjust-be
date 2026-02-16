package com.hongguoyan.module.biz.controller.admin.banner.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 轮播图 Response VO")
@Data
public class BannerRespVO {

    @Schema(description = "轮播图ID", example = "1024")
    private Long id;

    @Schema(description = "展示位置(1=首页,2=定制页)", example = "1")
    private Integer position;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "图片 path（入库值）")
    private String picPath;

    @Schema(description = "图片访问 URL（展示用）")
    private String picUrl;

    @Schema(description = "跳转类型(0=无,1=H5,2=小程序页面)", example = "2")
    private Integer linkType;

    @Schema(description = "跳转链接/小程序路径")
    private String linkUrl;

    @Schema(description = "按钮文案")
    private String ctaText;

    @Schema(description = "排序(越大越靠前)", example = "100")
    private Integer sort;

    @Schema(description = "状态(0=停用,1=启用)", example = "1")
    private Integer status;

    @Schema(description = "开始展示时间")
    private LocalDateTime startTime;

    @Schema(description = "结束展示时间")
    private LocalDateTime endTime;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}

