package com.qgexam.rabbit.config;

import com.qgexam.rabbit.constants.ExamBeginRabbitConstants;
import com.qgexam.rabbit.constants.ExamUnderwayRabbitConstants;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author yzw
 * @date 2023年01月24日 14:26
 */
@Configuration
public class ExamUnderwayRabbitConfig {
    @Bean
    public DirectExchange examUnderwayDirectExchange() {
        return new DirectExchange(ExamUnderwayRabbitConstants.EXAM_UNDERWAY_EXCHANGE_NAME, true, false);
    }
    @Bean
    public Queue examUnderwayQueue() {
        return new Queue(ExamUnderwayRabbitConstants.EXAM_UNDERWAY_QUEUE_NAME, true, false, false);
    }

    @Bean
    public Binding examUnderwayBinding(DirectExchange examUnderwayDirectExchange, Queue examUnderwayQueue) {
        return BindingBuilder.bind(examUnderwayQueue).to(examUnderwayDirectExchange).with(ExamUnderwayRabbitConstants.EXAM_UNDERWAY_ROUTING_KEY);
    }
}
