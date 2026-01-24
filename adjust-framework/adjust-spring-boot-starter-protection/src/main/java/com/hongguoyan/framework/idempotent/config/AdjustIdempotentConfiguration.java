package com.hongguoyan.framework.idempotent.config;

import com.hongguoyan.framework.idempotent.core.aop.IdempotentAspect;
import com.hongguoyan.framework.idempotent.core.keyresolver.impl.DefaultIdempotentKeyResolver;
import com.hongguoyan.framework.idempotent.core.keyresolver.impl.ExpressionIdempotentKeyResolver;
import com.hongguoyan.framework.idempotent.core.keyresolver.IdempotentKeyResolver;
import com.hongguoyan.framework.idempotent.core.keyresolver.impl.UserIdempotentKeyResolver;
import com.hongguoyan.framework.idempotent.core.redis.IdempotentRedisDAO;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import com.hongguoyan.framework.redis.config.AdjustRedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;

@AutoConfiguration(after = AdjustRedisAutoConfiguration.class)
public class AdjustIdempotentConfiguration {

    @Bean
    public IdempotentAspect idempotentAspect(List<IdempotentKeyResolver> keyResolvers, IdempotentRedisDAO idempotentRedisDAO) {
        return new IdempotentAspect(keyResolvers, idempotentRedisDAO);
    }

    @Bean
    public IdempotentRedisDAO idempotentRedisDAO(StringRedisTemplate stringRedisTemplate) {
        return new IdempotentRedisDAO(stringRedisTemplate);
    }

    // ========== 各种 IdempotentKeyResolver Bean ==========

    @Bean
    public DefaultIdempotentKeyResolver defaultIdempotentKeyResolver() {
        return new DefaultIdempotentKeyResolver();
    }

    @Bean
    public UserIdempotentKeyResolver userIdempotentKeyResolver() {
        return new UserIdempotentKeyResolver();
    }

    @Bean
    public ExpressionIdempotentKeyResolver expressionIdempotentKeyResolver() {
        return new ExpressionIdempotentKeyResolver();
    }

}
