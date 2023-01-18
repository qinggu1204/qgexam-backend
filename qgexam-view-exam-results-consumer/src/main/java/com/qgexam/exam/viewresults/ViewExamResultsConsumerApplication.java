package com.qgexam.exam.viewresults;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableDubbo
public class ViewExamResultsConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ViewExamResultsConsumerApplication.class);
    }
}
