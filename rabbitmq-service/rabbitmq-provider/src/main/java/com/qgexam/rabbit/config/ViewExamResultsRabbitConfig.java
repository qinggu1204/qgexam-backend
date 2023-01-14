package com.qgexam.rabbit.config;

import com.qgexam.rabbit.constants.ViewExamResultsRabbitConstant;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ljy
 * @date 2023年01月10日 9:56
 */
@Configuration
public class ViewExamResultsRabbitConfig {
    @Bean
    public DirectExchange examViewResultsExchange() {
        return new DirectExchange(ViewExamResultsRabbitConstant.EXAM_VIEWRESULTS_EXCHANGE_NAME, true, false);
    }
    @Bean
    public Queue examViewResultsQueue() {
        return new Queue(ViewExamResultsRabbitConstant.EXAM_RVIEWRESULTS_QUEUE_NAME, true, false, false);
    }

    @Bean
    public Binding examViewResultsBinding(DirectExchange examViewResultsExchange, Queue examViewResultsQueue) {
        return BindingBuilder.bind(examViewResultsQueue).to(examViewResultsExchange).with(ViewExamResultsRabbitConstant.EXAM_VIEWRESULTS_ROUTING_KEY);
    }
}
