package com.hongguoyan.module.biz.controller.admin.ai.doubao.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Map;

@Schema(description = "管理后台 - 豆包 Responses 创建响应")
@Data
public class DoubaoResponsesRespVO {

    @Schema(description = "响应 ID", example = "resp_xxx")
    private String responseId;

    @Schema(description = "输出文本", example = "……")
    private String outputText;

    @Schema(description = "少量原始字段（便于排查问题，避免敏感信息）")
    private Map<String, Object> raw;
}

