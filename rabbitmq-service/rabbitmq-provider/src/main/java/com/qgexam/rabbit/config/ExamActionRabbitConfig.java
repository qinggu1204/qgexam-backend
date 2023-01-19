package com.qgexam.rabbit.config;

import com.qgexam.rabbit.constants.ExamActionRabbitConstant;
import com.qgexam.rabbit.constants.ExamRecordRabbitConstant;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author yzw
 * @date 2023年01月13日 10:48
 */
@Configuration
public class ExamActionRabbitConfig {
    @Bean
    public DirectExchange screenCuttingDirectExchange() {
        return new DirectExchange(ExamRecordRabbitConstant.EXAM_ACTION_EXCHANGE_NAME, true, false);
    }
    @Bean
    public Queue screenCuttingQueue() {
        return new Queue(ExamRecordRabbitConstant.EXAM_ACTION_QUEUE_NAME, true, false, false);
    }

    @Bean
    public Binding screenCuttingBinding(DirectExchange screenCuttingDirectExchange, Queue screenCuttingQueue) {
        return BindingBuilder.bind(screenCuttingQueue).to(screenCuttingDirectExchange).with(ExamRecordRabbitConstant.EXAM_ACTION_ROUTING_KEY);
    }

    @Bean
    public DirectExchange examFaceComparisonDirectExchange() {
        return new DirectExchange(ExamActionRabbitConstant.EXAM_ACTION_FACE_EXCHANGE_NAME, true, false);
    }
    @Bean
    public Queue examFaceComparisonQueue() {
        return new Queue(ExamActionRabbitConstant.EXAM_ACTION_FACE_QUEUE_NAME, true, false, false);
    }

    @Bean
    public Binding examFaceComparisonBinding(DirectExchange examFaceComparisonDirectExchange, Queue examFaceComparisonQueue) {
        return BindingBuilder.bind(examFaceComparisonQueue).to(examFaceComparisonDirectExchange).with(ExamActionRabbitConstant.EXAM_ACTION_FACE_ROUTING_KEY);
    }

}
