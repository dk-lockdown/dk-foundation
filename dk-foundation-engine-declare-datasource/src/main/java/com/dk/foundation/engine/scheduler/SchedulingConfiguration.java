package com.dk.foundation.engine.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

/**
 * Created by duguk on 2018/1/8.
 */
@Configuration
@EnableScheduling
public class SchedulingConfiguration  implements SchedulingConfigurer {
    final static Logger logger = LoggerFactory.getLogger(SchedulingConfiguration.class);

    @Value("${system.schedule.poolsize:5}")
    private Integer systemSchedulePoolsize;

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.setTaskScheduler(taskScheduler());
    }

    @Bean(destroyMethod = "shutdown")
    public ThreadPoolTaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(systemSchedulePoolsize);
        scheduler.setThreadNamePrefix("dispatch-");
        scheduler.setAwaitTerminationSeconds(600);
        scheduler.setErrorHandler(throwable -> logger.error("调度任务发生异常", throwable));
        scheduler.setWaitForTasksToCompleteOnShutdown(true);
        return scheduler;
    }

}
