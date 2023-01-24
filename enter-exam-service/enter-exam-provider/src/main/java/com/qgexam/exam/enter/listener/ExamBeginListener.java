package com.qgexam.exam.enter.listener;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.qgexam.common.core.api.AppHttpCodeEnum;
import com.qgexam.common.core.constants.ExamConstants;
import com.qgexam.common.core.constants.MessageConstants;
import com.qgexam.common.core.exception.BusinessException;
import com.qgexam.common.core.utils.BeanCopyUtils;
import com.qgexam.common.redis.utils.RedisCache;
import com.qgexam.exam.enter.dao.*;
import com.qgexam.exam.enter.pojo.PO.ExaminationInfo;
import com.qgexam.exam.enter.pojo.PO.ExaminationPaper;
import com.qgexam.exam.enter.pojo.PO.MessageInfo;
import com.qgexam.exam.enter.pojo.PO.QuestionInfo;
import com.qgexam.exam.enter.pojo.VO.OptionInfoVO;
import com.qgexam.exam.enter.pojo.VO.QuestionInfoVO;
import com.qgexam.exam.enter.pojo.VO.SubQuestionInfoVO;
import com.qgexam.rabbit.constants.ExamBeginRabbitConstants;



import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author yzw
 * @date 2023年01月24日 14:28
 */
@Slf4j
@Component
public class ExamBeginListener {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ExaminationInfoDao examinationInfoDao;

    @Autowired
    private ExaminationPaperDao examinationPaperDao;

    @Autowired
    private OptionInfoDao optionInfoDao;

    @Autowired
    private SubQuestionInfoDao subQuestionInfoDao;

    @Autowired
    private ThreadPoolExecutor executor;

    @Autowired
    private CourseInfoDao courseInfoDao;

    @Autowired
    private StudentInfoDao studentInfoDao;

    @Autowired
    private MessageInfoDao messageInfoDao;


    @RabbitListener(queues = ExamBeginRabbitConstants.EXAM_BEGIN_QUEUE_NAME)
    public void listenExamRecordQueue(Integer examinationId, Channel channel, Message message) throws IOException {
        log.info("###########ExamBeginListener.listenExamRecordQueue()###########");
        // 根据考试Id查询考试信息
        ExaminationInfo examinationInfo = examinationInfoDao.getByExaminationId(examinationId);
        // 如果examinationInfo为空，抛出BusinessException
        if (examinationInfo == null) {
            throw new BusinessException(AppHttpCodeEnum.SYSTEM_ERROR.getCode(), "考试不存在");
        }

        CompletableFuture<Void> setExaminationPaperFuture = CompletableFuture.runAsync(() -> {
            setExaminationPaper(examinationInfo);
        }, executor);

        CompletableFuture<Void> sentMessageFeture = CompletableFuture.runAsync(() -> {
            sentMessage(examinationInfo);
        }, executor);

        CompletableFuture.allOf(setExaminationPaperFuture, sentMessageFeture).whenComplete((res, exception) -> {
            if (exception != null) {
                log.error("考试开始缓存消息监听异常", exception);
                try {
                    channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
                } catch (IOException e) {
                    log.error("考试开始消息监听异常", e);
                }
            } else {
                try {
                    log.info("考试开始消息监听成功");
                    channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                } catch (IOException e) {
                    log.error("考试开始消息监听异常", e);
                }
            }
        });

    }

    private void setExaminationPaper(ExaminationInfo examinationInfo) {
        System.out.println("===========ExamBeginListener.setExaminationPaper=============");
        // 根据examinationInfo.paperId查询试卷信息
        // 获取试卷Id
        Integer examinationPaperId = examinationInfo.getExaminationPaperId();
        // 查询试卷，同时查询试卷中的题目
        ExaminationPaper examinationPaper = examinationPaperDao.selectExaminationPaperById(examinationPaperId);
        // 获取题目list
        List<QuestionInfo> questionInfoList = examinationPaper.getQuestionInfoList();
        questionInfoList.stream()
                .forEach(questionInfo -> {
                    // 获取题目Id
                    Integer questionId = questionInfo.getQuestionId();
                    // 根据题目Id查询选项
                    List<OptionInfoVO> optionInfos = optionInfoDao.selectOptionInfoListByQuestionInfoId(questionId);
                    // 根据题目Id查询小题
                    List<SubQuestionInfoVO> subQuestionInfos = subQuestionInfoDao.selectSubQuestionInfoListByQuestionInfoId(questionId);
                    questionInfo.setOptionInfo(optionInfos);
                    questionInfo.setSubQuestionInfo(subQuestionInfos);
                });
        // 从questionInfoList过滤出不同题型
        List<QuestionInfo> singleQuestionInfoList = questionInfoList.stream()
                .filter(questionInfo -> ExamConstants.QUESTION_TYPE_SINGLE.equals(questionInfo.getType()))
                .collect(Collectors.toList());

        List<QuestionInfo> multipleQuestionInfoList = questionInfoList.stream()
                .filter(questionInfo -> ExamConstants.QUESTION_TYPE_MULTI.equals(questionInfo.getType()))
                .collect(Collectors.toList());

        List<QuestionInfo> judgeQuestionInfoList = questionInfoList.stream()
                .filter(questionInfo -> ExamConstants.QUESTION_TYPE_JUDGE.equals(questionInfo.getType()))
                .collect(Collectors.toList());

        List<QuestionInfo> completionQuestionInfoList = questionInfoList.stream()
                .filter(questionInfo -> ExamConstants.QUESTION_TYPE_COMPLETION.equals(questionInfo.getType()))
                .collect(Collectors.toList());

        List<QuestionInfo> complexQuestionInfoList = questionInfoList.stream()
                .filter(questionInfo -> ExamConstants.QUESTION_TYPE_COMPLEX.equals(questionInfo.getType()))
                .collect(Collectors.toList());

        // 将题目信息放入map中，方便下一步存入redis
        // list转换为map
        Map<String, QuestionInfoVO> singleMap = singleQuestionInfoList.stream()
                .collect(Collectors.toMap(
                        questionInfo -> questionInfo.getQuestionId().toString(),
                        questionInfo -> BeanCopyUtils.copyBean(questionInfo, QuestionInfoVO.class)
                ));

        Map<String, QuestionInfoVO> multiMap = multipleQuestionInfoList.stream()
                .collect(Collectors.toMap(
                        questionInfo -> questionInfo.getQuestionId().toString(),
                        questionInfo -> BeanCopyUtils.copyBean(questionInfo, QuestionInfoVO.class)
                ));
        Map<String, QuestionInfoVO> judgeMap = judgeQuestionInfoList.stream()
                .collect(Collectors.toMap(
                        questionInfo -> questionInfo.getQuestionId().toString(),
                        questionInfo -> BeanCopyUtils.copyBean(questionInfo, QuestionInfoVO.class)
                ));

        Map<String, QuestionInfoVO> completionMap = completionQuestionInfoList.stream()
                .collect(Collectors.toMap(
                        questionInfo -> questionInfo.getQuestionId().toString(),
                        questionInfo -> BeanCopyUtils.copyBean(questionInfo, QuestionInfoVO.class)
                ));

        Map<String, QuestionInfoVO> complexMap = complexQuestionInfoList.stream()
                .collect(Collectors.toMap(
                        questionInfo -> questionInfo.getQuestionId().toString(),
                        questionInfo -> BeanCopyUtils.copyBean(questionInfo, QuestionInfoVO.class)
                ));


        // 获取考试结束时间
        LocalDateTime endTime = examinationInfo.getEndTime();
        // 获取考试结束时间和当前时间的时间差
        Duration duration = LocalDateTimeUtil.between(LocalDateTime.now(), endTime);
        // 获取时间差的毫秒数，作为redis超时时间，单位为毫秒
        long timeout = duration.toMillis() + 60000;

        // 将考试信息存入redis
        redisCache.setCacheObject(ExamConstants.EXAMINATION_INFO_HASH_KEY_PREFIX + examinationInfo.getExaminationId(), examinationInfo);
        redisCache.expire(ExamConstants.EXAMINATION_INFO_HASH_KEY_PREFIX + examinationInfo.getExaminationId(), timeout, TimeUnit.MILLISECONDS);
        // 将题目信息存入redis
        // 单选题
        redisCache.setCacheMap(ExamConstants.EXAMINATION_SINGLE_QUESTION_HASH_FIELD + examinationInfo.getExaminationId(), singleMap);
        // 设置超时时间
        redisCache.expire(ExamConstants.EXAMINATION_SINGLE_QUESTION_HASH_FIELD + examinationInfo.getExaminationId(), timeout, TimeUnit.MILLISECONDS);

        redisCache.setCacheMap(ExamConstants.EXAMINATION_MULTI_QUESTION_HASH_FIELD + examinationInfo.getExaminationId(), multiMap);
        redisCache.expire(ExamConstants.EXAMINATION_MULTI_QUESTION_HASH_FIELD + examinationInfo.getExaminationId(), timeout, TimeUnit.MILLISECONDS);


        redisCache.setCacheMap(ExamConstants.EXAMINATION_JUDGE_QUESTION_HASH_FIELD + examinationInfo.getExaminationId(), judgeMap);
        redisCache.expire(ExamConstants.EXAMINATION_JUDGE_QUESTION_HASH_FIELD + examinationInfo.getExaminationId(), timeout, TimeUnit.MILLISECONDS);

        redisCache.setCacheMap(ExamConstants.EXAMINATION_COMPLETION_QUESTION_HASH_FIELD + examinationInfo.getExaminationId(), completionMap);
        redisCache.expire(ExamConstants.EXAMINATION_COMPLETION_QUESTION_HASH_FIELD + examinationInfo.getExaminationId(), timeout, TimeUnit.MILLISECONDS);

        redisCache.setCacheMap(ExamConstants.EXAMINATION_COMPLEX_QUESTION_HASH_FIELD + examinationInfo.getExaminationId(), complexMap);
        redisCache.expire(ExamConstants.EXAMINATION_COMPLEX_QUESTION_HASH_FIELD + examinationInfo.getExaminationId(), timeout, TimeUnit.MILLISECONDS);

    }

    private void sentMessage(ExaminationInfo examinationInfo) {
        System.out.println("===============ExamBeginListener.sentMessage=================");
        Integer examinationId = examinationInfo.getExaminationId();
        // 根据考试id查询courseId
        List<Integer> courseIdList = examinationInfoDao.selectCourseIdByExaminationId(examinationId);
        // 根据courseId查询所有的studentId
        List<Integer> studentIdList = courseInfoDao.selectStudentIdListByCourseIds(courseIdList);
        // 根据studentId查询所有的userId
        List<Integer> userIdList = studentInfoDao.selectUserIdListByStudentIdList(studentIdList);
        // 封装消息对象
        List<MessageInfo> messageInfoList = userIdList.stream()
                .map(userId -> {
                    MessageInfo messageInfo = new MessageInfo();
                    messageInfo.setUserId(userId);
                    messageInfo.setTitle(MessageConstants.EXAMINATION_NOTICE);
                    messageInfo.setExaminationName(examinationInfo.getExaminationName());
                    messageInfo.setStartTime(examinationInfo.getStartTime());
                    messageInfo.setEndTime(examinationInfo.getEndTime());
                    return messageInfo;
                }).collect(Collectors.toList());
        // 向消息表中插入消息
        messageInfoDao.insertBache(messageInfoList);

    }
}
