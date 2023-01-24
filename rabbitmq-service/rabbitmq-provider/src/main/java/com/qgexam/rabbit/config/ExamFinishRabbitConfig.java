package com.qgexam.rabbit.config;

import com.qgexam.rabbit.constants.ExamFinishRabbitConstants;
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
public class ExamFinishRabbitConfig {
    @Bean
    public DirectExchange examFinishDirectExchange() {
        return new DirectExchange(ExamFinishRabbitConstants.EXAM_FINISH_EXCHANGE_NAME, true, false);
    }
    @Bean
    public Queue examFinishQueue() {
        return new Queue(ExamFinishRabbitConstants.EXAM_FINISH_QUEUE_NAME, true, false, false);
    }

    @Bean
    public Binding examFinishBinding(DirectExchange examFinishDirectExchange, Queue examFinishQueue) {
        return BindingBuilder.bind(examFinishQueue).to(examFinishDirectExchange).with(ExamFinishRabbitConstants.EXAM_FINISH_ROUTING_KEY);
    }
}
