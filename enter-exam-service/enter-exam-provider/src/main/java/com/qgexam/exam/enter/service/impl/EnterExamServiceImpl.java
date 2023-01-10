package com.qgexam.exam.enter.service.impl;


import cn.hutool.core.date.LocalDateTimeUtil;
import com.qgexam.common.core.api.AppHttpCodeEnum;
import com.qgexam.common.core.constants.ExamConstants;
import com.qgexam.common.core.exception.BusinessException;
import com.qgexam.common.redis.utils.RedisCache;
import com.qgexam.exam.enter.dao.ExaminationInfoDao;
import com.qgexam.exam.enter.pojo.PO.ExaminationInfo;
import com.qgexam.exam.enter.pojo.VO.*;
import com.qgexam.exam.enter.service.EnterExamService;
import com.qgexam.user.pojo.VO.ExaminationInfoVO;
import com.qgexam.user.pojo.VO.QuestionInfoVO;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

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

    @Override
    public GetExaminationPaperVO getExaminationPaper(Integer examinationId) {
        // 从redis中读出考试信息
        ExaminationInfoVO examinationInfo = redisCache.getCacheObject(ExamConstants.EXAMINATION_INFO_HASH_KEY_PREFIX + examinationId);
        // 获取限时进入时间
        Integer limitTime = examinationInfo.getLimitTime();
        // 等于0说明不限时进入
        if (limitTime != 0) {
            LocalDateTime startTime = examinationInfo.getStartTime();
            // 计算限时进入时间点
            LocalDateTime enterLimitTime = LocalDateTimeUtil.offset(startTime, limitTime, ChronoUnit.MINUTES);
            // 判断当前时间是否在限时进入时间之前
            if (!LocalDateTime.now().isBefore(enterLimitTime)) {
                throw new BusinessException(AppHttpCodeEnum.SYSTEM_ERROR.getCode(), "晚于考试限时进入时间，无法进入考试。");
            }
        }

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
        if(singleMap == null || multiMap == null || judgeMap == null || completionMap == null || complexMap == null) {
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

}
