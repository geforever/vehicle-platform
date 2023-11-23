package org.platform.vehicle.config;

import java.util.concurrent.ThreadPoolExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @Author gejiawei
 * @Date 2021/3/24 3:25 下午
 */

@Configuration
public class ThreadPoolConfig {

    @Bean(name = "asyncTaskExecutor")
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor threadPool = new ThreadPoolTaskExecutor();
        // 核心线程数
        threadPool.setCorePoolSize(5);
        // 最大线程数
        threadPool.setMaxPoolSize(20);
        // 任务队列大小
        threadPool.setQueueCapacity(100);
        // 线程前缀名
        threadPool.setThreadNamePrefix("async-thread-pool");
        // 线程的空闲时间
        threadPool.setKeepAliveSeconds(100);
        // 拒绝策略 交由调用方线程运行
        threadPool.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 线程初始化
        threadPool.initialize();
        return threadPool;
    }

    @Bean(name = "esTaskExecutor")
    public ThreadPoolTaskExecutor jdbcThreadPoolTaskExecutor() {
        ThreadPoolTaskExecutor threadPool = new ThreadPoolTaskExecutor();
        // 核心线程数
        threadPool.setCorePoolSize(5);
        // 最大线程数
        threadPool.setMaxPoolSize(20);
        // 任务队列大小
        threadPool.setQueueCapacity(1000);
        // 线程前缀名
        threadPool.setThreadNamePrefix("es-thread-pool");
        // 线程的空闲时间
        threadPool.setKeepAliveSeconds(100);
        // 拒绝策略 交由调用方线程运行
        threadPool.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 线程初始化
        threadPool.initialize();
        return threadPool;
    }

}
