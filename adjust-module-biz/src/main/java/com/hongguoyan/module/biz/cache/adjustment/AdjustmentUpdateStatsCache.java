package com.hongguoyan.module.biz.cache.adjustment;

import com.hongguoyan.module.biz.cache.CacheNames;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * 调剂更新统计-缓存清理。
 */
@Service
@Validated
public class AdjustmentUpdateStatsCache {

    @CacheEvict(cacheNames = CacheNames.ADJUSTMENT_UPDATE_STATS, key = "'default'")
    public void evictDefault() {
        // no-op (used for cache eviction)
    }
}

