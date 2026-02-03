package com.hongguoyan.module.biz.service.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * AI text generation request.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiTextRequest {

    /**
     * Optional provider hint, e.g. "doubao".
     */
    private String provider;

    /**
     * Optional model id. When blank, provider default should be used.
     */
    private String model;

    /**
     * Prompt text.
     */
    private String prompt;

    /**
     * Optional image urls for multimodal requests (provider dependent).
     */
    private List<String> imageUrls;

    /**
     * Optional file urls to be attached (provider dependent).
     */
    private List<String> fileUrls;

    /**
     * Optional request timeout in milliseconds.
     */
    private Long timeoutMs;
}

