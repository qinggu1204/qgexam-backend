package com.qgexam.exam.finish.service;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
@MapperScan(basePackages = "com.qgexam.exam.finish.dao")
public class FinishExamProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(FinishExamProviderApplication.class);
    }
}
