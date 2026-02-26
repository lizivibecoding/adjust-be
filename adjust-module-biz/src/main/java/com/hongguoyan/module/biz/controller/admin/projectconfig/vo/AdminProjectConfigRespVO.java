package com.hongguoyan.module.biz.controller.admin.projectconfig.vo;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 项目配置 Response VO")
@Data
@JsonPropertyOrder({"adjustYear", "activeYear", "doubao", "hasApiKey", "apiKeyMasked"})
public class AdminProjectConfigRespVO {

    @Schema(description = "调剂年（用于调剂数据统计/展示）", example = "2026")
    private Integer adjustYear;

    @Schema(description = "字典年（用于 biz_major 等基础数据）", example = "2026")
    private Integer activeYear;

    @Schema(description = "豆包配置")
    private Doubao doubao;

    @Schema(description = "是否已配置 API Key（仅提示）", example = "true")
    private Boolean hasApiKey;

    @Schema(description = "API Key 脱敏回显（仅提示）", example = "sk-****4578")
    private String apiKeyMasked;

    @Data
    public static class Doubao {

        @Schema(description = "Base URL", example = "https://ark.cn-beijing.volces.com/api")
        private String baseUrl;

        @Schema(description = "默认模型 ID", example = "doubao-seed-1-6-250615")
        private String defaultModel;

        @Schema(description = "默认超时（毫秒）", example = "60000")
        private Long defaultTimeoutMs;
    }
}

