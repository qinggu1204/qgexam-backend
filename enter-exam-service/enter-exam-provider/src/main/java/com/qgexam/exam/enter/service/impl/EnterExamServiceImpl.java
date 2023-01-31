package com.qgexam.exam.enter.service.impl;


import cn.hutool.core.date.LocalDateTimeUtil;
import com.qgexam.common.core.api.AppHttpCodeEnum;
import com.qgexam.common.core.constants.ExamConstants;
import com.qgexam.common.core.exception.BusinessException;
import com.qgexam.common.core.utils.BeanCopyUtils;
import com.qgexam.common.redis.utils.RedisCache;

import com.qgexam.exam.enter.dao.ExaminationInfoDao;
import com.qgexam.exam.enter.dao.ExaminationPaperDao;
import com.qgexam.exam.enter.pojo.DTO.JoinExamDTO;

import com.qgexam.exam.enter.pojo.PO.ExaminationInfo;
import com.qgexam.exam.enter.pojo.PO.ExaminationPaper;
import com.qgexam.exam.enter.pojo.VO.*;
import com.qgexam.exam.enter.service.EnterExamService;


import com.sun.org.apache.xpath.internal.operations.Bool;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * @author yzw
 * @date 2023年01月09日 19:06
 */
@DubboService
public class EnterExamServiceImpl implements EnterExamService {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ExaminationInfoDao examinationInfoDao;

    @Autowired
    private ExaminationPaperDao examinationPaperDao;

    @Override
    public GetExaminationPaperVO getExaminationPaper(JoinExamDTO joinExamDTO) {
        Integer examinationId = joinExamDTO.getExaminationId();
        LocalDateTime joinTime = joinExamDTO.getJoinTime();
        Integer studentId = joinExamDTO.getStudentId();
        // 判断当前考试是否合法
        ExaminationInfo examinationInfo = isExamInvalid(examinationId, joinTime, studentId);

        String singlePrefix = ExamConstants.EXAMINATION_SINGLE_QUESTION_HASH_FIELD + examinationId;
        String multiPrefix = ExamConstants.EXAMINATION_MULTI_QUESTION_HASH_FIELD + examinationId;
        String judgePrefix = ExamConstants.EXAMINATION_JUDGE_QUESTION_HASH_FIELD + examinationId;
        String completionPrefix = ExamConstants.EXAMINATION_COMPLETION_QUESTION_HASH_FIELD + examinationId;
        String complexPrefix = ExamConstants.EXAMINATION_COMPLEX_QUESTION_HASH_FIELD + examinationId;
        Map<String, QuestionInfoVO> singleMap = redisCache.getCacheMap(singlePrefix);
        Map<String, QuestionInfoVO> multiMap = redisCache.getCacheMap(multiPrefix);
        Map<String, QuestionInfoVO> judgeMap = redisCache.getCacheMap(judgePrefix);
        Map<String, QuestionInfoVO> completionMap = redisCache.getCacheMap(completionPrefix);
        Map<String, QuestionInfoVO> complexMap = redisCache.getCacheMap(complexPrefix);
        if (singleMap == null || multiMap == null || judgeMap == null || completionMap == null || complexMap == null) {
            throw new BusinessException(AppHttpCodeEnum.SYSTEM_ERROR.getCode(), "考试试卷不存在");
        }
        // 为空则返回空list
        List<QuestionInfoVO> singleList = new ArrayList<>(singleMap.values());
        List<QuestionInfoVO> multiList = new ArrayList<>(multiMap.values());
        List<QuestionInfoVO> judgeList = new ArrayList<>(judgeMap.values());
        List<QuestionInfoVO> completionList = new ArrayList<>(completionMap.values());
        List<QuestionInfoVO> complexList = new ArrayList<>(complexMap.values());
        // 选项乱序
        Integer isOptionResort = examinationInfo.getIsOptionResort();
        if (ExamConstants.OPTION_RESORT.equals(isOptionResort)) {
            singleList.stream()
                    .map(questionInfoVO -> questionInfoVO.getOptionInfo())
                    .forEach(optionInfoVOS -> {
                        Collections.shuffle(optionInfoVOS);
                    });
            multiList.stream()
                    .map(questionInfoVO -> questionInfoVO.getOptionInfo())
                    .forEach(optionInfoVOS -> {
                        Collections.shuffle(optionInfoVOS);
                    });
            judgeList.stream()
                    .map(questionInfoVO -> questionInfoVO.getOptionInfo())
                    .forEach(optionInfoVOS -> {
                        Collections.shuffle(optionInfoVOS);
                    });
        }
        // 题目乱序
        Integer isQuestionResort = examinationInfo.getIsQuestionResort();
        if (ExamConstants.QUESTION_RESORT.equals(isQuestionResort)) {
            Collections.shuffle(singleList);
            Collections.shuffle(multiList);
            Collections.shuffle(judgeList);
            Collections.shuffle(completionList);
            Collections.shuffle(complexList);
        }
        GetExaminationPaperVO getExaminationPaperVO = new GetExaminationPaperVO();
        getExaminationPaperVO.setSingle(new SingleVO(ExamConstants.QUESTION_TYPE_SINGLE, singleList));
        getExaminationPaperVO.setMulti(new MultipleVO(ExamConstants.QUESTION_TYPE_MULTI, multiList));
        getExaminationPaperVO.setJudge(new JudgeVO(ExamConstants.QUESTION_TYPE_JUDGE, judgeList));
        getExaminationPaperVO.setCompletion(new CompletionVO(ExamConstants.QUESTION_TYPE_COMPLETION, completionList));
        getExaminationPaperVO.setComplex(new ComplexVO(ExamConstants.QUESTION_TYPE_COMPLEX, complexList));

        return getExaminationPaperVO;
    }

    @Override
    public GetExaminationInfoVO getExaminationInfo(JoinExamDTO joinExamDTO) {
        Integer examinationId = joinExamDTO.getExaminationId();
        LocalDateTime joinTime = joinExamDTO.getJoinTime();
        // 判断当前考试是否合法
        ExaminationInfo examinationInfo = redisCache.getCacheObject(ExamConstants.EXAMINATION_INFO_HASH_KEY_PREFIX + examinationId);
        if (examinationInfo == null) {
            examinationInfo = examinationInfoDao.selectById(examinationId);
        }
        // 获取考试信息
        Integer examinationPaperId = examinationInfo.getExaminationPaperId();
        ExaminationPaper examinationPaper = examinationPaperDao.selectById(examinationPaperId);
        Integer totalScore = examinationPaper.getTotalScore();
        GetExaminationInfoVO examinationInfoVO = BeanCopyUtils.copyBean(examinationInfo, GetExaminationInfoVO.class);
        examinationInfoVO.setTotalScore(totalScore);
        return examinationInfoVO;
    }

    @Override
    public void screenCutting(JoinExamDTO joinExamDTO) {
        Integer examinationId = joinExamDTO.getExaminationId();
        LocalDateTime joinTime = joinExamDTO.getJoinTime();
        Integer studentId = joinExamDTO.getStudentId();
        // 判断当前考试是否合法
        ExaminationInfo examinationInfo = isExamInvalid(examinationId, joinTime, studentId, 1);
        LocalDateTime endTime = examinationInfo.getEndTime();
        Duration duration = LocalDateTimeUtil.between(LocalDateTime.now(), endTime);
        long timeout = duration.toMillis();
        // 如果合法则则将redis中的切屏次数+1
        String screenCuttingKey = ExamConstants.SCREEN_CUTTING_KEY + examinationId + ":" + studentId;
        Integer cuttingNumber = redisCache.getCacheObject(screenCuttingKey);
        if (cuttingNumber == null) {
            cuttingNumber = 0;
        }
        cuttingNumber++;
        redisCache.setCacheObject(screenCuttingKey, cuttingNumber);
        redisCache.expire(screenCuttingKey, timeout);
    }

    @Override
    public Integer getScreenCuttingNumber(JoinExamDTO joinExamDTO) {
        Integer studentId = joinExamDTO.getStudentId();
        Integer examinationId = joinExamDTO.getExaminationId();
        LocalDateTime joinTime = joinExamDTO.getJoinTime();
        // 判断当前考试是否合法
        ExaminationInfo examinationInfo = isExamInvalid(examinationId, joinTime, studentId, 1);

        String cuttingKey = ExamConstants.SCREEN_CUTTING_KEY + examinationId + ":" + studentId;
        Integer cuttingNumber = redisCache.getCacheObject(cuttingKey);
        // 如果cuttingNumber为null则说明没有切屏
        if (cuttingNumber == null) {
            cuttingNumber = 0;
        }
        return cuttingNumber;
    }


    private ExaminationInfo isExamInvalid(Integer examinationId, LocalDateTime joinTime, Integer studentId) {
        // 从redis中读出考试信息
        ExaminationInfo examinationInfo = redisCache.getCacheObject(ExamConstants.EXAMINATION_INFO_HASH_KEY_PREFIX + examinationId);

        /*
         * 1.考试未开始(未到开始前10分钟)，数据内容还未加载到redis中
         * 2.考试已结束，数据内容已从redis中删除
         */
        if (examinationInfo == null) {
            // 从数据库查询考试消息
            ExaminationInfo tempExaminationInfo = examinationInfoDao.selectById(examinationId);
            // 考试不存在
            if (tempExaminationInfo == null) {
                throw new BusinessException(AppHttpCodeEnum.SYSTEM_ERROR.getCode(), "考试不存在");
            }
            LocalDateTime startTime = tempExaminationInfo.getStartTime();
            LocalDateTime endTime = tempExaminationInfo.getEndTime();
            // 考试未开始
            if (joinTime.isBefore(startTime)) {
                throw new BusinessException(AppHttpCodeEnum.SYSTEM_ERROR.getCode(), "考试还未开始，无法进入考试。");
            }
            // 考试已结束
            if (joinTime.isAfter(endTime)) {
                throw new BusinessException(AppHttpCodeEnum.SYSTEM_ERROR.getCode(), "考试已结束。");
            }
        }
        // 运行到此处说明redis中examinationInfo不为空
        /*
        需要考虑以下情况：
            1.试卷已经加在到redis中但是未开始考试
            2.试卷已经加载到redis中但是考试已经结束(考试结束后1min缓存考试消息才会过期)
            // 上面1，2能够保证考试一定在进行中
            考虑以下情况：
            3.考试已经开始但是超过了限时进入时间
            4.考试未结束，但是学生已经提交试卷

         */

        LocalDateTime endTime = examinationInfo.getEndTime();
        Integer limitTime = examinationInfo.getLimitTime();
        LocalDateTime startTime = examinationInfo.getStartTime();

        // 试卷已经加在到redis中但是未开始考试
        if (joinTime.isBefore(startTime)) {
            throw new BusinessException(AppHttpCodeEnum.SYSTEM_ERROR.getCode(), "考试还未开始，无法进入考试。");
        }

        // 试卷已经加载到redis中但是考试已经结束(非必要,因为考试结束后会将redis中的数据删除)
        if (joinTime.isAfter(endTime)) {
            throw new BusinessException(AppHttpCodeEnum.SYSTEM_ERROR.getCode(), "考试已经结束。");
        }

        // 考试已经开始但是超过了限时进入时间
        // 等于0说明不限时进入
        if (limitTime != 0) {
            // 计算限时进入时间点
            LocalDateTime enterLimitTime = LocalDateTimeUtil.offset(startTime, limitTime, ChronoUnit.MINUTES);
            // 判断当前时间是否在限时进入时间之前
            if (!joinTime.isBefore(enterLimitTime)) {
                throw new BusinessException(AppHttpCodeEnum.SYSTEM_ERROR.getCode(), "晚于考试限时进入时间，无法进入考试。");
            }
        }
        // 考试未结束，但是学生已经提交试卷
        String submitKey = ExamConstants.EXAMINATIONANSWER_SUBMIT_KEY_PREFIX + examinationId + ":" + studentId;
        // 从缓存中取出答卷提交标记
        Object cacheObject = redisCache.getCacheObject(submitKey);
        // 如果不为空说明学生已经提交试卷
        if (cacheObject != null) {
            throw new BusinessException(AppHttpCodeEnum.SYSTEM_ERROR.getCode(), "已经提交答卷，无法再次进入考试。");
        }

        return examinationInfo;
    }


    private ExaminationInfo isExamInvalid(Integer examinationId, LocalDateTime joinTime, Integer studentId, Integer screenCutting) {
        // 从redis中读出考试信息
        ExaminationInfo examinationInfo = redisCache.getCacheObject(ExamConstants.EXAMINATION_INFO_HASH_KEY_PREFIX + examinationId);

        /*
         * 1.考试未开始(未到开始前10分钟)，数据内容还未加载到redis中
         * 2.考试已结束，数据内容已从redis中删除
         */
        if (examinationInfo == null) {
            // 从数据库查询考试消息
            ExaminationInfo tempExaminationInfo = examinationInfoDao.selectById(examinationId);
            // 考试不存在
            if (tempExaminationInfo == null) {
                throw new BusinessException(AppHttpCodeEnum.SYSTEM_ERROR.getCode(), "考试不存在");
            }
            LocalDateTime startTime = tempExaminationInfo.getStartTime();
            LocalDateTime endTime = tempExaminationInfo.getEndTime();
            // 考试未开始
            if (joinTime.isBefore(startTime)) {
                throw new BusinessException(AppHttpCodeEnum.SYSTEM_ERROR.getCode(), "考试还未开始，无法进入考试。");
            }
            // 考试已结束
            if (joinTime.isAfter(endTime)) {
                throw new BusinessException(AppHttpCodeEnum.SYSTEM_ERROR.getCode(), "考试已结束。");
            }
        }
        // 运行到此处说明redis中examinationInfo不为空
        /*
        需要考虑以下情况：
            1.试卷已经加在到redis中但是未开始考试
            2.试卷已经加载到redis中但是考试已经结束(考试结束后1min缓存考试消息才会过期)
            // 上面1，2能够保证考试一定在进行中
            考虑以下情况：
            3.考试已经开始但是超过了限时进入时间
            4.考试未结束，但是学生已经提交试卷

         */

        LocalDateTime endTime = examinationInfo.getEndTime();
        Integer limitTime = examinationInfo.getLimitTime();
        LocalDateTime startTime = examinationInfo.getStartTime();

        // 试卷已经加在到redis中但是未开始考试
        if (joinTime.isBefore(startTime)) {
            throw new BusinessException(AppHttpCodeEnum.SYSTEM_ERROR.getCode(), "考试还未开始，无法进入考试。");
        }

        // 试卷已经加载到redis中但是考试已经结束(非必要,因为考试结束后会将redis中的数据删除)
        if (joinTime.isAfter(endTime)) {
            throw new BusinessException(AppHttpCodeEnum.SYSTEM_ERROR.getCode(), "考试已经结束。");
        }


        // 考试未结束，但是学生已经提交试卷
        String submitKey = ExamConstants.EXAMINATIONANSWER_SUBMIT_KEY_PREFIX + examinationId + ":" + studentId;
        // 从缓存中取出答卷提交标记
        Object cacheObject = redisCache.getCacheObject(submitKey);
        // 如果不为空说明学生已经提交试卷
        if (cacheObject != null) {
            throw new BusinessException(AppHttpCodeEnum.SYSTEM_ERROR.getCode(), "已经提交答卷，无法再次进入考试。");
        }

        return examinationInfo;
    }


    @Override
    public Boolean checkExam(JoinExamDTO joinExamDTO) {
        Integer studentId = joinExamDTO.getStudentId();
        Integer examinationId = joinExamDTO.getExaminationId();
        LocalDateTime joinTime = joinExamDTO.getJoinTime();
        // 判断当前考试是否合法
        ExaminationInfo examinationInfo = isExamInvalid(examinationId, joinTime, studentId);
        return true;
    }
}
