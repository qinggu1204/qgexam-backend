package com.qgexam.user;

import cn.hutool.extra.spring.SpringUtil;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

@Import(SpringUtil.class)
@SpringBootApplication
@EnableDubbo
@EnableScheduling
@MapperScan("com.qgexam.user.dao")
public class UserProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserProviderApplication.class);
    }
}
