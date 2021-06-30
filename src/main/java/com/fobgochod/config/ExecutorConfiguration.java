package com.fobgochod.config;

import com.fobgochod.exception.BusinessException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.RejectedExecutionHandler;

/**
 * 线程池
 *
 * @author zhouxiao
 * @date 2020/5/11
 */
@Configuration
public class ExecutorConfiguration {

    @Bean(name = "threadPoolTaskExecutor")
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        // 核心线程数
        taskExecutor.setCorePoolSize(10);
        // 最大线程数
        taskExecutor.setMaxPoolSize(10);
        // 线程最大空闲时间
        taskExecutor.setKeepAliveSeconds(60);
        // 队列容量
        taskExecutor.setQueueCapacity(100);
        // 线程名称
        taskExecutor.setThreadNamePrefix("dmc-task-exec-");
        // 队列满，线程被拒绝执行策略
        taskExecutor.setRejectedExecutionHandler(this.getRejectedExecutionHandler());
        return taskExecutor;
    }

    @Bean
    public ThreadPoolTaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(2);
        taskScheduler.setThreadNamePrefix("dmc-task-scheduler-");
        taskScheduler.setWaitForTasksToCompleteOnShutdown(true);
        taskScheduler.setAwaitTerminationSeconds(60);
        return taskScheduler;
    }

    /**
     * 拒绝策略
     *
     * @return
     */
    private RejectedExecutionHandler getRejectedExecutionHandler() {
        return (r, executor) -> {
            throw new BusinessException("当前处理任务过多，请稍后再试");
        };
    }
}
