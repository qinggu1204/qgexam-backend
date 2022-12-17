package com.qgexam.answerPapers.marking;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
@MapperScan(basePackages = "com.qgexam.answerPapers.marking.dao")
public class MarkingAnswerPapersProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(MarkingAnswerPapersProviderApplication.class);
    }
}
