package com.qgexam.user.task.config;

import lombok.SneakyThrows;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class QuartzConfig {
    @Autowired
    private MyQuartzJobFactory myQuartzJobFactory;

    @SneakyThrows
    @Bean
    public Scheduler scheduler(){
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();
        // 自定义 JobFactory 使得在 Quartz Job 中可以使用 @Autowired
        scheduler.setJobFactory(myQuartzJobFactory);
        scheduler.start();
        return scheduler;
    }
}
