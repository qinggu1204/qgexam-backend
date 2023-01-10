package com.qgexam.quartz.job;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.qgexam.common.core.constants.ExamConstants;
import com.qgexam.common.core.utils.BeanCopyUtils;
import com.qgexam.common.redis.utils.RedisCache;

import com.qgexam.quartz.dao.ExaminationInfoDao;
import com.qgexam.quartz.dao.ExaminationPaperDao;
import com.qgexam.quartz.dao.OptionInfoDao;
import com.qgexam.quartz.dao.SubQuestionInfoDao;
import com.qgexam.user.pojo.PO.*;
import com.qgexam.user.pojo.VO.ExaminationInfoVO;
import com.qgexam.user.pojo.VO.OptionInfoVO;
import com.qgexam.user.pojo.VO.QuestionInfoVO;
import com.qgexam.user.pojo.VO.SubQuestionInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author yzw
 * @date 2023年01月08日 23:42
 */
@Component("examBeginJob")
public class ExamBeginJob {


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

    public void execute(Integer examinationId) {
        System.out.println("###########examBeginJob.execute()###########");
        // 根据考试Id查询考试信息
        ExaminationInfo examinationInfo = examinationInfoDao.getByExaminationId(examinationId);
        // 如果examinationInfo为空，抛出BusinessException
        if (examinationInfo == null) {
            throw new RuntimeException("考试不存在");
        }
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

        // 获取考试信息
        ExaminationInfoVO examinationInfoVO = BeanCopyUtils.copyBean(examinationInfo, ExaminationInfoVO.class);
        // 将数字的考试状态转换为中文
        if(ExamConstants.EXAM_STATUS_NOT_START.equals(examinationInfo.getStatus())){
            examinationInfoVO.setStatus(ExamConstants.EXAM_STATUS_NOT_START_VO);
        }else if(ExamConstants.EXAM_STATUS_UNDERWAY.equals(examinationInfo.getStatus())){
            examinationInfoVO.setStatus(ExamConstants.EXAM_STATUS_UNDERWAY_VO);
        }else if(ExamConstants.EXAM_STATUS_OVER.equals(examinationInfo.getStatus())){
            examinationInfoVO.setStatus(ExamConstants.EXAM_STATUS_OVER_VO);
        }

        // 获取考试结束时间
        LocalDateTime endTime = examinationInfo.getEndTime();
        // 获取考试结束时间和当前时间的时间差
        Duration duration = LocalDateTimeUtil.between(LocalDateTime.now(), endTime);
        // 获取时间差的毫秒数，作为redis超时时间，单位为毫秒
        long timeout = duration.toMillis();

        // 将考试信息存入redis
        redisCache.setCacheObject(ExamConstants.EXAMINATION_INFO_HASH_KEY_PREFIX + examinationInfo.getExaminationId(), examinationInfoVO);
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
}
