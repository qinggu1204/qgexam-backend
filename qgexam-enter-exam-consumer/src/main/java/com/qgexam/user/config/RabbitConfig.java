package com.qgexam.user.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @author yzw
 * @date 2023年01月06日 17:53
 */
@Slf4j
@Configuration
public class RabbitConfig implements RabbitTemplate.ReturnCallback{

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @PostConstruct
    public void initRabbit(){
        rabbitTemplate.setReturnCallback(this);
    }

    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        log.info("消息发送失败，应答码{}，原因{}，交换机{}，路由键{},消息{}",
                replyCode, replyText, exchange, routingKey, message.toString());
        // 消息发送失败则重发消息
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }


}
