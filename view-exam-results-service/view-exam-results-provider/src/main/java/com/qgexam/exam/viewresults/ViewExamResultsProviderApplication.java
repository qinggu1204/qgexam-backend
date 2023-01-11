package com.qgexam.exam.viewresults;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
@MapperScan(basePackages = "com.qgexam.exam.viewresults.dao")
public class ViewExamResultsProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(ViewExamResultsProviderApplication.class);
    }
}
