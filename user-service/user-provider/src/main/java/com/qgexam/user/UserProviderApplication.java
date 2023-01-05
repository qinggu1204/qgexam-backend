package com.qgexam.user;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Arrays;

@SpringBootApplication
@EnableDubbo
@MapperScan(basePackages = "com.qgexam.user.dao")
public class UserProviderApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(UserProviderApplication.class);
        String[] names = ctx.getBeanDefinitionNames();
        Arrays.stream(names).forEach(System.out::println);
    }
}
