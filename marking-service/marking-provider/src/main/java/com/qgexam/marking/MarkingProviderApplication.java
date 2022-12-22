package com.qgexam.marking;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
@MapperScan(basePackages = "com.qgexam.marking.dao")
public class MarkingProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(MarkingProviderApplication.class);
    }
}
