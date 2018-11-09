package com.mzkj.mock_trading_system.common.scheduling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import static com.mzkj.mock_trading_system.common.consts.Message.SCHEDULER_UNCAUGHT_EXCEPTION;

/**
 * clock的定时任务多线程运行
 */
@Configuration
public class MultiThreadsSchedulingConfigurer implements SchedulingConfigurer {

    private Logger logger = LoggerFactory.getLogger(MultiThreadsSchedulingConfigurer.class);

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(10);
        taskScheduler.setBeanName("MultiThreadsSchedulingConfigurer");
        taskScheduler.setErrorHandler((Throwable t) -> {
            String threadName = Thread.currentThread().getName();
            logger.error(SCHEDULER_UNCAUGHT_EXCEPTION.statusCode + "|" + threadName, t);
        });
        taskScheduler.initialize();
        taskRegistrar.setTaskScheduler(taskScheduler);
    }
}
