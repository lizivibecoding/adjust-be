package com.hongguoyan.module.biz.controller.app.adjustment.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = "用户 APP - 调剂筛选配置 Response VO")
@Data
public class AppAdjustmentFilterConfigRespVO {

    @Schema(description = "分组列表(顺序与筛选器 UI 保持一致)")
    private List<Group> groups;

    @Schema(description = "筛选分组")
    @Data
    public static class Group {

        @Schema(description = "分组 key", example = "publishTime")
        private String key;

        @Schema(description = "分组名称", example = "发布时间")
        private String name;

        @Schema(description = "选项列表(无子分组时使用)")
        private List<Option> options;

        @Schema(description = "子分组列表(如 A区/B区)")
        private List<Group> children;
    }

    @Schema(description = "筛选选项")
    @Data
    public static class Option {

        @Schema(description = "选项 code(统一用字符串，前端按需转型)", example = "7")
        private String code;

        @Schema(description = "选项名称", example = "近7天")
        private String name;
    }
}

