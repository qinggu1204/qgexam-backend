package com.qgexam.common.quartz.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import javax.sql.DataSource;

@Configuration
public class SchedulerConfig {

  @Bean
  public SchedulerFactoryBean scheduler(DataSource dataSource) {
    SchedulerFactoryBean factory = new SchedulerFactoryBean();
    factory.setConfigLocation(new ClassPathResource("quartz.properties"));
    factory.setDataSource(dataSource);
    factory.setJobFactory(new SpringBeanJobFactory());
    // 延时启动
    factory.setStartupDelay(1);
    factory.setApplicationContextSchedulerContextKey("applicationContextKey");
    // 可选，QuartzScheduler
    // 启动时更新己存在的Job，这样就不用每次修改targetObject后删除qrtz_job_details表对应记录了
    factory.setOverwriteExistingJobs(true);
    // 设置自动启动，默认为true
    factory.setAutoStartup(true);
    return factory;
  }

}
