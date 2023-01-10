package com.qgexam.quartz;

import cn.hutool.extra.spring.SpringUtil;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

/**
 * @author yzw
 * @date 2023年01月10日 18:24
 */
@Import(SpringUtil.class)
@SpringBootApplication
@EnableDubbo
@MapperScan(basePackages = "com.qgexam.quartz.dao")
public class QuartzProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(QuartzProviderApplication.class);
    }
}
