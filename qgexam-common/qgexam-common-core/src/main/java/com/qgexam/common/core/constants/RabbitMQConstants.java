package com.qgexam.common.core.constants;

public class RabbitMQConstants {
    /**
     * 阅卷结束提醒查询成绩模块开始缓存的交换机名称
     */
    public static final String BEGIN_CACHE_EXCHANGE_NAME = "beginCache.exchange";

    /**
     * 阅卷结束提醒查询成绩模块开始缓存的队列名称
     */
    public static final String BEGIN_CACHE_QUEUE_NAME = "beginCache.queue";

    /**
     * 阅卷结束提醒查询成绩模块开始缓存的匹配规则
     */
    public static final String BEGIN_CACHE_ROUTING_KEY = "beginCache";

    /**
     * 阅卷结束提醒查询成绩模块开始缓存的消息
     */
    public static final String BEGIN_CACHE_MESSAGE = "OK";
}