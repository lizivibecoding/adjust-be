package com.hongguoyan.module.biz.framework.job.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration(proxyBeanMethods = false)
public class BizJobConfiguration {

    public static final String RECOMMEND_PDF_THREAD_POOL_TASK_EXECUTOR = "RECOMMEND_PDF_THREAD_POOL_TASK_EXECUTOR";

    @Bean(RECOMMEND_PDF_THREAD_POOL_TASK_EXECUTOR)
    public ThreadPoolTaskExecutor recommendPdfThreadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10); // 设置核心线程数
        executor.setMaxPoolSize(10); // 设置最大线程数
        executor.setKeepAliveSeconds(60); // 设置空闲时间
        executor.setQueueCapacity(Integer.MAX_VALUE); // 无界队列
        executor.setThreadNamePrefix("recommend-pdf-task-"); // 配置线程池的前缀
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

}
