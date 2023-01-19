package com.qgexam.rabbit.config;

import com.qgexam.rabbit.constants.BeginCacheRabbitConstants;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeginCacheRabbitConfig {
    @Bean
    public Queue beginCacheQueue(){
        return new Queue(BeginCacheRabbitConstants.BEGIN_CACHE_QUEUE_NAME,true,false,false);
    }

    @Bean
    public DirectExchange beginCacheDirectExchange(){
        return new DirectExchange(BeginCacheRabbitConstants.BEGIN_CACHE_EXCHANGE_NAME,true,false);
    }

    @Bean
    public Binding beginCacheBingingDirect(){
        return BindingBuilder.bind(beginCacheQueue())
                .to(beginCacheDirectExchange())
                .with(BeginCacheRabbitConstants.BEGIN_CACHE_ROUTING_KEY);
    }
}
