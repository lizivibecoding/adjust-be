package com.hongguoyan.module.biz.framework.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Global adjust config (shared across biz module).
 */
@Data
@Component
@ConfigurationProperties(prefix = "adjust")
public class AdjustProperties {

    /**
     * Active academic/business year for dictionary queries.
     */
    private Integer activeYear = 2026;
}

