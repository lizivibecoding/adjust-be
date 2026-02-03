package com.hongguoyan.module.biz.service.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * AI text generation result.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiTextResult {

    /**
     * Output text.
     */
    private String text;

    /**
     * Minimal raw fields for debugging (avoid sensitive information).
     */
    private Map<String, Object> raw;
}

