package com.qgexam.user;

import cn.hutool.extra.spring.SpringUtil;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
@Import(SpringUtil.class)
@SpringBootApplication
@EnableDubbo
@MapperScan(basePackages = {"com.qgexam.user.dao", "com.qgexam.common.quartz.dao"})
@ComponentScan(basePackages = {"com.qgexam.user", "com.qgexam.common.quartz"})
public class UserProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserProviderApplication.class);
    }
}
