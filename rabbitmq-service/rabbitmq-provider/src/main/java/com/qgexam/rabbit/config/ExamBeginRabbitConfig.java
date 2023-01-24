package com.qgexam.rabbit.config;

import com.qgexam.rabbit.constants.ExamBeginRabbitConstants;
import com.qgexam.rabbit.constants.ExamRecordRabbitConstant;
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
public class ExamBeginRabbitConfig {
    @Bean
    public DirectExchange examBeginDirectExchange() {
        return new DirectExchange(ExamBeginRabbitConstants.EXAM_BEGIN_EXCHANGE_NAME, true, false);
    }
    @Bean
    public Queue examBeginQueue() {
        return new Queue(ExamBeginRabbitConstants.EXAM_BEGIN_QUEUE_NAME, true, false, false);
    }

    @Bean
    public Binding examBeginBinding(DirectExchange examBeginDirectExchange, Queue examBeginQueue) {
        return BindingBuilder.bind(examBeginQueue).to(examBeginDirectExchange).with(ExamBeginRabbitConstants.EXAM_BEGIN_ROUTING_KEY);
    }
}
