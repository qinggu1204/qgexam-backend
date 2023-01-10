package com.qgexam.exam.enter.config;

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


}
