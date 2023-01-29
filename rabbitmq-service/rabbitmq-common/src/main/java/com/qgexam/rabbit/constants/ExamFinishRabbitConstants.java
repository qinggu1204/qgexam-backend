package com.qgexam.rabbit.constants;

/**
 * @author yzw
 * @date 2023年01月24日 14:26
 */
public class ExamFinishRabbitConstants {
    public static final String EXAM_FINISH_EXCHANGE_NAME = "exam.underway.exchange";
    public static final String EXAM_FINISH_QUEUE_NAME = "exam.underway.queue";
    public static final String EXAM_FINISH_ROUTING_KEY = "exam.underway";
}
