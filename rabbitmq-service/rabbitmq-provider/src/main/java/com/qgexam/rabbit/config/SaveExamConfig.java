package com.qgexam.rabbit.config;

import com.qgexam.rabbit.constants.FinishExamRabbitConstants;
import com.qgexam.rabbit.constants.SaveExamRabbitConstants;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author tageshi
 * @date 2023/1/20 16:27
 */
@Configuration
public class SaveExamConfig {
    @Bean
    public DirectExchange examSaveExchange() {
        return new DirectExchange(SaveExamRabbitConstants.EXAM_SAVE_EXCHANGE_NAME, true, false);
    }
    @Bean
    public Queue examSaveQueue() {
        return new Queue(SaveExamRabbitConstants.EXAM_SAVE_NAME, true, false, false);
    }
    @Bean
    public Binding examSaveBinding(DirectExchange examSaveExchange, Queue examSaveQueue) {
        return BindingBuilder.bind(examSaveQueue).to(examSaveExchange).with(SaveExamRabbitConstants.EXAM_SAVE_ROUTING_KEY);
    }
}
