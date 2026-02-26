package com.hongguoyan.module.biz.controller.admin.projectconfig.vo;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Schema(description = "管理后台 - 项目配置更新 Request VO")
@Data
@JsonPropertyOrder({"adjustYear", "activeYear", "doubao"})
public class AdminProjectConfigUpdateReqVO {

    @Schema(description = "调剂年（用于调剂数据统计/展示）", requiredMode = Schema.RequiredMode.REQUIRED, example = "2026")
    @NotNull(message = "adjustYear 不能为空")
    private Integer adjustYear;

    @Schema(description = "字典年（用于 biz_major 等基础数据）", requiredMode = Schema.RequiredMode.REQUIRED, example = "2026")
    @NotNull(message = "activeYear 不能为空")
    private Integer activeYear;

    @Schema(description = "豆包配置", requiredMode = Schema.RequiredMode.REQUIRED)
    @Valid
    @NotNull(message = "doubao 不能为空")
    private Doubao doubao;

    @Data
    public static class Doubao {

        @Schema(description = "Base URL", example = "https://ark.cn-beijing.volces.com/api")
        @Size(max = 300, message = "baseUrl 不能超过 300 个字符")
        private String baseUrl;

        @Schema(description = "API Key：null 表示不修改，\"\" 表示清空", example = "sk-***")
        @Size(max = 200, message = "apiKey 不能超过 200 个字符")
        private String apiKey;

        @Schema(description = "默认模型 ID", example = "doubao-seed-1-6-250615")
        @Size(max = 100, message = "defaultModel 不能超过 100 个字符")
        private String defaultModel;

        @Schema(description = "默认超时（毫秒）", example = "60000")
        @Min(value = 1, message = "defaultTimeoutMs 必须大于 0")
        private Long defaultTimeoutMs;
    }
}

