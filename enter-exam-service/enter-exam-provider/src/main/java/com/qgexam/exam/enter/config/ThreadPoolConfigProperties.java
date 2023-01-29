package com.qgexam.exam.enter.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author yzw
 * @date 2023年01月24日 10:00
 */
@Component
@ConfigurationProperties(prefix = "qgexam.thread")
@Data
public class ThreadPoolConfigProperties {
    private int corePoolSize;
    private int maxPoolSize;
    private long keepAliveTime;
    private int queueCapacity;
}
