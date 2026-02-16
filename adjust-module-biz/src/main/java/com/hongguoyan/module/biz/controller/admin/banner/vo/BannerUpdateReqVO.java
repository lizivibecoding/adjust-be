package com.hongguoyan.module.biz.controller.admin.banner.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 轮播图更新 Request VO")
@Data
public class BannerUpdateReqVO {

    @Schema(description = "轮播图ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotNull(message = "ID 不能为空")
    private Long id;

    @Schema(description = "展示位置(1=首页,2=定制页)", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "展示位置不能为空")
    private Integer position;

    @Schema(description = "标题", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "标题不能为空")
    private String title;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "图片 path（上传接口返回的 path）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "图片不能为空")
    private String picPath;

    @Schema(description = "跳转类型(0=无,1=H5,2=小程序页面)", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    @NotNull(message = "跳转类型不能为空")
    private Integer linkType;

    @Schema(description = "跳转链接/小程序路径")
    private String linkUrl;

    @Schema(description = "按钮文案")
    private String ctaText;

    @Schema(description = "排序(越大越靠前)", requiredMode = Schema.RequiredMode.REQUIRED, example = "100")
    @NotNull(message = "排序不能为空")
    private Integer sort;

    @Schema(description = "状态(0=停用,1=启用)", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "状态不能为空")
    private Integer status;

    @Schema(description = "开始展示时间")
    private LocalDateTime startTime;

    @Schema(description = "结束展示时间")
    private LocalDateTime endTime;
}

