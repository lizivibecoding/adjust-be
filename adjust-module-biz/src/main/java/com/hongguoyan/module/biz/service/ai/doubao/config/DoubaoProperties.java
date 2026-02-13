package com.hongguoyan.module.biz.service.ai.doubao.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Doubao (Volcengine Ark) config.
 *
 * <p>Bind from:
 * <ul>
 *   <li>adjust.ai.doubao.base-url</li>
 *   <li>adjust.ai.doubao.api-key</li>
 *   <li>adjust.ai.doubao.default-model</li>
 *   <li>adjust.ai.doubao.default-timeout-ms</li>
 * </ul>
 */
@Data
@Component
@ConfigurationProperties(prefix = "adjust.ai.doubao")
public class DoubaoProperties {

    /**
     * Base URL without /v3 suffix.
     * <p>Default: https://ark.cn-beijing.volces.com/api
     */
    private String baseUrl = "https://ark.cn-beijing.volces.com/api";

    /**
     * Ark API key. Never commit.
     */
    private String apiKey;

    /**
     * Default model id.
     */
    private String defaultModel;

    /**
     * Default single request timeout.
     */
    private Long defaultTimeoutMs = 60_000L;
}

