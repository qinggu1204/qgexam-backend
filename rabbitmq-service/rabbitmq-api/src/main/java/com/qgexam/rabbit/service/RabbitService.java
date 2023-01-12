package com.qgexam.rabbit.service;

/**
 * @author yzw
 * @date 2023年01月11日 9:18
 */
public interface RabbitService {
    <T> void sendMessage(String exchange, String routingKey, T message);
}
