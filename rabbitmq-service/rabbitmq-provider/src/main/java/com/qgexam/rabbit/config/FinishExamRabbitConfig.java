package com.qgexam.rabbit.config;

import com.qgexam.rabbit.constants.ExamRecordRabbitConstant;
import com.qgexam.rabbit.constants.FinishExamRabbitConstants;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author tageshi
 * @date 2023/1/13 18:18
 */
@Configuration
public class FinishExamRabbitConfig {
    @Bean
    public DirectExchange examFinishExchange() {
        return new DirectExchange(FinishExamRabbitConstants.EXAM_FINISH_EXCHANGE_NAME, true, false);
    }

    @Bean
    public Queue examFinishQueue() {
        return new Queue(FinishExamRabbitConstants.EXAM_FINISH_NAME, true, false, false);
    }

    @Bean
    public Binding examFinishBinding(DirectExchange examFinishExchange, Queue examFinishQueue) {
        return BindingBuilder.bind(examFinishQueue).to(examFinishExchange).with(FinishExamRabbitConstants.EXAM_FINISH_ROUTING_KEY);
    }
}
