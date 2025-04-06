package com.xiong.bitmanager.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @ClassName ThreadPoolConfig
 * @Description TODO
 * @Author admin
 * @Date 2025/3/13 14:32
 * @Version 1.0
 **/
@Configuration
public class ThreadPoolConfig {
    // 延迟调度线程池（1-2个线程即可）
    @Bean("schedulerPool")
    public ScheduledExecutorService schedulerPool() {
        return Executors.newScheduledThreadPool(1);
    }

    // 动态业务线程池
    @Bean("bizExecutor")
    public ThreadPoolTaskExecutor bizExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(5);
        executor.setQueueCapacity(1000);
        executor.setThreadNamePrefix("BizPool-");
        executor.initialize();
        return executor;
    }
}
