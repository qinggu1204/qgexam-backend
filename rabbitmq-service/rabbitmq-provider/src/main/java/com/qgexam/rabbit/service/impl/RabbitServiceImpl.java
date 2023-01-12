package com.qgexam.rabbit.service.impl;

import com.qgexam.rabbit.config.ExamRecordRabbitConfig;
import com.qgexam.rabbit.service.RabbitService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

/**
 * @author yzw
 * @date 2023年01月11日 9:18
 */
@DubboService
@Slf4j
public class RabbitServiceImpl implements RabbitService {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public <T> void sendMessage(String exchange, String routingKey, T message) {
        // 消息唯一id
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());

        correlationData.getFuture().addCallback(
                result -> {
                    if (result.isAck()) {
                        log.debug("消息发送成功, 消息id:{}", correlationData.getId());
                    } else {
                        log.error("消息发送失败, 消息id:{}, 原因:{}", correlationData.getId(), result.getReason());
                        // 重发消息
                        rabbitTemplate.convertAndSend(exchange, routingKey, message, correlationData);
                    }
                },
                ex -> {
                    log.error("消息发送失败, 消息id:{}, 原因:{}", correlationData.getId(), ex.getMessage());
                    // 重发消息
                    rabbitTemplate.convertAndSend(exchange, routingKey, message, correlationData);
                }
        );
        // 发送消息
        rabbitTemplate.convertAndSend(exchange, routingKey, message, correlationData);
    }
}
