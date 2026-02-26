package com.hongguoyan.module.biz.controller.app.adjustment.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = "调剂详情切换选项-响应")
@Data
public class AppAdjustmentOptionsRespVO {

    @Schema(description = "选项列表（按年份倒序）")
    private List<Option> options;

    @Data
    public static class Option {

        @Schema(description = "年份", example = "2025")
        private Integer year;

        @Schema(description = "展示文案", example = "官方发布调剂")
        private String text;

    }

}

