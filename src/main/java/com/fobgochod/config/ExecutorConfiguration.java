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
        taskExecutor.setCorePoolSize(10);
        taskExecutor.setMaxPoolSize(10);
        taskExecutor.setKeepAliveSeconds(60);
        taskExecutor.setQueueCapacity(100);
        taskExecutor.setThreadNamePrefix("task-exec-");
        taskExecutor.setRejectedExecutionHandler(this.getRejectedExecutionHandler());
        return taskExecutor;
    }

    @Bean
    public ThreadPoolTaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(2);
        taskScheduler.setThreadNamePrefix("task-scheduler-");
        taskScheduler.setWaitForTasksToCompleteOnShutdown(true);
        taskScheduler.setAwaitTerminationSeconds(60);
        return taskScheduler;
    }

    private RejectedExecutionHandler getRejectedExecutionHandler() {
        return (r, executor) -> {
            throw new BusinessException("当前处理任务过多，请稍后再试");
        };
    }
}
