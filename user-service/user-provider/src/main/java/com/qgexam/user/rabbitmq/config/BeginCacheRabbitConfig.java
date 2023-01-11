package com.qgexam.user.rabbitmq.config;

import com.qgexam.common.core.constants.RabbitMQConstants;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeginCacheRabbitConfig {
    @Bean
    public Queue beginCacheQueue(){
        return new Queue(RabbitMQConstants.BEGIN_CACHE_QUEUE_NAME);
    }

    @Bean
    public DirectExchange beginCacheDirectExchange(){
        return new DirectExchange(RabbitMQConstants.BEGIN_CACHE_EXCHANGE_NAME);
    }

    @Bean
    public Binding beginCacheBingingDirect(){
        return BindingBuilder.bind(beginCacheQueue())
                .to(beginCacheDirectExchange())
                .with(RabbitMQConstants.BEGIN_CACHE_ROUTING_KEY);
    }
}
