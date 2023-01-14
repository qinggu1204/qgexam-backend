package com.qgexam.rabbit.config;

import com.qgexam.rabbit.constants.ExamRecordRabbitConstant;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author yzw
 * @date 2023年01月10日 9:56
 */
@Configuration
public class ExamRecordRabbitConfig {

    @Bean
    public DirectExchange examRecordDirectExchange() {
        return new DirectExchange(ExamRecordRabbitConstant.EXAM_RECORD_EXCHANGE_NAME, true, false);
    }
    @Bean
    public Queue examRecordQueue() {
        return new Queue(ExamRecordRabbitConstant.EXAM_RECORD_QUEUE_NAME, true, false, false);
    }

    @Bean
    public Binding examRecordBinding(DirectExchange examRecordDirectExchange, Queue examRecordQueue) {
        return BindingBuilder.bind(examRecordQueue).to(examRecordDirectExchange).with(ExamRecordRabbitConstant.EXAM_RECORD_ROUTING_KEY);
    }


}
