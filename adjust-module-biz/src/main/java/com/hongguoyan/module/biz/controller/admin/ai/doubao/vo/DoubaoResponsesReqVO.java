package com.hongguoyan.module.biz.controller.admin.ai.doubao.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Schema(description = "管理后台 - 豆包 Responses 创建请求")
@Data
public class DoubaoResponsesReqVO {

    @Schema(description = "模型 ID（可选，默认取配置 biz.ai.doubao.default-model）", example = "doubao-seed-1-6-250615")
    private String model;

    @Schema(description = "用户文本输入", requiredMode = Schema.RequiredMode.REQUIRED, example = "你好呀。")
    @NotBlank(message = "文本输入不能为空")
    private String text;

    @Schema(description = "图片 URL 列表（可选，多模态时使用）")
    private List<String> imageUrls;

    @Schema(description = "文件 URL 列表（可选，v1 将作为文本附加）")
    private List<String> fileUrls;

    @Schema(description = "超时时间（毫秒，可选）", example = "60000")
    @Min(value = 1000, message = "timeoutMs 不能小于 1000")
    private Long timeoutMs;
}

