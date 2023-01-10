package com.qgexam.exam.enter.config.rabbit;

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

    public static final String EXAM_RECORD_EXCHANGE_NAME = "exam.enter.record.exchange";

    public static final String EXAM_RECORD_QUEUE_NAME = "exam.enter.record.queue";

    public static final String EXAM_RECORD_ROUTING_KEY = "exam.enter.record";


    @Bean
    public DirectExchange examRecordDirectExchange() {
        return new DirectExchange(EXAM_RECORD_EXCHANGE_NAME, true, false);
    }
    @Bean
    public Queue examRecordQueue() {
        return new Queue(EXAM_RECORD_QUEUE_NAME, true, false, false);
    }

    @Bean
    public Binding examRecordBinding(DirectExchange examRecordDirectExchange, Queue examRecordQueue) {
        return BindingBuilder.bind(examRecordQueue).to(examRecordDirectExchange).with(EXAM_RECORD_ROUTING_KEY);
    }







}
