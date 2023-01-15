package com.qgexam.exam.viewresults.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qgexam.common.core.api.AppHttpCodeEnum;
import com.qgexam.common.core.constants.ExamConstants;
import com.qgexam.common.core.exception.BusinessException;
import com.qgexam.common.redis.utils.RedisCache;
import com.qgexam.exam.viewresults.dao.*;
import com.qgexam.exam.viewresults.pojo.PO.*;
import com.qgexam.exam.viewresults.pojo.VO.*;
import com.qgexam.exam.viewresults.service.AnswerPaperInfoService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 答卷表(AnswerPaperInfo)表服务实现类
 *
 * @author ljy
 * @since 2023-01-10 16:35:22
 */
@DubboService
public class AnswerPaperInfoServiceImpl extends ServiceImpl<AnswerPaperInfoDao, AnswerPaperInfo> implements AnswerPaperInfoService {
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private ExaminationInfoDao examinationInfoDao;
    @Autowired
    private ExaminationPaperDao examinationPaperDao;
    @Autowired
    private AnswerPaperInfoDao answerPaperInfoDao;
    @Autowired
    private AnswerPaperDetailDao answerPaperDetailDao;
    @Autowired
    private OptionInfoDao optionInfoDao;
    @Autowired
    private SubQuestionInfoDao subQuestionInfoDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ExamScoreDetailVO getExamScoreDetail(Integer examinationId, Integer studentId){
        // 创建考试成绩明细对象
        ExamScoreDetailVO examScoreDetailVO = new ExamScoreDetailVO();
        // 从缓存里取查询成绩的时间
        String strQueryTime = redisCache.getCacheObject(ExamConstants.EXAMRESULT_QUERYTIME_HASH_KEY_PREFIX + examinationId);
        // 判断是否到查询成绩的时间了
        // 成绩查询未开始
        if(strQueryTime != null) {
            if (strQueryTime.equals("123")) {
                throw new BusinessException(AppHttpCodeEnum.SYSTEM_ERROR.getCode(), "成绩查询还未开始，无法进入查询页面。");
            }
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime resultQueryTime = LocalDateTime.parse(strQueryTime, df);
            // 成绩查询未开始
            if (LocalDateTime.now().isBefore(resultQueryTime)) {
                throw new BusinessException(AppHttpCodeEnum.SYSTEM_ERROR.getCode(), "成绩查询还未开始，无法进入查询页面。");
            }
            // 查询缓存里的考试总分和答卷总分
            Integer totalScore = redisCache.getCacheObject(ExamConstants.EXAMRESULT_TOTALSCORE_HASH_KEY_PREFIX + examinationId);
            Integer stuTotalScore = redisCache.getCacheObject(ExamConstants.EXAMRESULT_STUTOTALSCORE_HASH_KEY_PREFIX + examinationId + ExamConstants.EXAMRESULT_STUID_HASH_KEY_PREFIX + studentId);
            // 设置考试总分和答卷总分
            examScoreDetailVO.setTotalScore(totalScore);
            examScoreDetailVO.setStuTotalScore(stuTotalScore);
            // 从缓存查考试题目成绩明细
            String singlePrefix = ExamConstants.EXAMRESULT_SINGLE_QUESTION_HASH_FIELD + examinationId + ExamConstants.EXAMRESULT_STUID_HASH_KEY_PREFIX + studentId;
            String multiPrefix = ExamConstants.EXAMRESULT_MULTI_QUESTION_HASH_FIELD + examinationId + ExamConstants.EXAMRESULT_STUID_HASH_KEY_PREFIX + studentId;
            String judgePrefix = ExamConstants.EXAMRESULT_JUDGE_QUESTION_HASH_FIELD + examinationId + ExamConstants.EXAMRESULT_STUID_HASH_KEY_PREFIX + studentId;
            String completionPrefix = ExamConstants.EXAMRESULT_COMPLETION_QUESTION_HASH_FIELD + examinationId + ExamConstants.EXAMRESULT_STUID_HASH_KEY_PREFIX + studentId;
            String complexPrefix = ExamConstants.EXAMRESULT_COMPLEX_QUESTION_HASH_FIELD + examinationId + ExamConstants.EXAMRESULT_STUID_HASH_KEY_PREFIX + studentId;
            Map<String, ObjResultVO> singleMap = redisCache.getCacheMap(singlePrefix);
            Map<String, ObjResultVO> multiMap = redisCache.getCacheMap(multiPrefix);
            Map<String, ObjResultVO> judgeMap = redisCache.getCacheMap(judgePrefix);
            Map<String, ObjResultVO> completionMap = redisCache.getCacheMap(completionPrefix);
            Map<String, SubResultVO> complexMap = redisCache.getCacheMap(complexPrefix);
            // 创建不同题型的list
            List<ObjResultVO> singleList1 = new ArrayList<>(singleMap.values());
            List<ObjResultVO> multiList1 = new ArrayList<>(multiMap.values());
            List<ObjResultVO> judgeList1 = new ArrayList<>(judgeMap.values());
            List<ObjResultVO> completionList1 = new ArrayList<>(completionMap.values());
            List<SubResultVO> complexList1 = new ArrayList<>(complexMap.values());
            examScoreDetailVO.setSingle(new SingleVO(singleList1));
            examScoreDetailVO.setMulti(new MultiVO(multiList1));
            examScoreDetailVO.setJudge(new JudgeVO(judgeList1));
            examScoreDetailVO.setCompletion(new CompletionVO(completionList1));
            examScoreDetailVO.setComplex(new ComplexVO(complexList1));
            return examScoreDetailVO;
        }
        // 如果找不到缓存里的查询成绩时间就说明查询成绩时间已到且已经过了高并发阶段，就去数据库查找成绩明细
        else {
            // 根据考试Id查询考试信息
            ExaminationInfo examinationInfo = examinationInfoDao.selectById(examinationId);
            // 如果examinationInfo为空，抛出BusinessException
            if (examinationInfo == null) {
                throw new RuntimeException("考试不存在");
            }
            // 根据examinationInfo.paperId查询试卷信息
            // 获取试卷Id
            Integer examinationPaperId = examinationInfo.getExaminationPaperId();
            // 查询试卷，同时查询试卷中的题目
            ExaminationPaper examinationPaper = examinationPaperDao.selectExaminationPaperById(examinationPaperId);
            // 查询学生的答卷
            AnswerPaperInfo answerPaperInfo = answerPaperInfoDao.selectStuAnswerPaper(examinationId,studentId);
            // 设置考试总分和答卷总分
            examScoreDetailVO.setTotalScore(examinationPaper.getTotalScore());
            examScoreDetailVO.setStuTotalScore(answerPaperInfo.getPaperTotalScore());
            // 获取题目list
            List<QuestionInfo> questionInfoList = examinationPaper.getQuestionInfoList();
            // 创建不同题型的list
            List<ObjResultVO> singleList = new ArrayList<>();
            List<ObjResultVO> multiList = new ArrayList<>();
            List<ObjResultVO> judgeList = new ArrayList<>();
            List<ObjResultVO> completionList = new ArrayList<>();
            List<SubResultVO> complexList = new ArrayList<>();
            questionInfoList.stream()
                    .forEach(questionInfo -> {
                        // 获取题目Id
                        Integer questionId = questionInfo.getQuestionId();
                        // 根据题目Id和答卷Id查询学生的答卷明细表
                        AnswerPaperDetail answerPaperDetail = answerPaperDetailDao.selectStuAnswerDetail(answerPaperInfo.getAnswerPaperId(),questionId);
                        // 根据题目Id查询选项
                        List<OptionResultVO> optionInfos = optionInfoDao.selectOptionInfoListByQuestionInfoId(questionId);
                        // 创建小题明细对象
                        List<SubQuestionResultVO> subQuestionInfos;
                        // 创建客观题明细对象
                        ObjResultVO objResultVO;
                        // 创建主观题明细对象
                        SubResultVO subResultVO;
                        // 判断是否有答卷明细表
                        if(answerPaperDetail != null)
                        {
                            // 根据题目Id和答卷Id查询小题和答卷小题
                            subQuestionInfos = subQuestionInfoDao.selectSubQuestionInfoList(answerPaperDetail.getAnswerPaperDetailId(),questionId);
                            // 创建客观题和主观题对象
                            objResultVO = new ObjResultVO(questionId, questionInfo.getDescription(), questionInfo.getQuestionScore(), answerPaperDetail.getScore(), answerPaperDetail.getStudentAnswer(), questionInfo.getQuestionAns(), optionInfos);
                            subResultVO = new SubResultVO(questionId, questionInfo.getDescription(), questionInfo.getQuestionScore(), answerPaperDetail.getScore(), questionInfo.getHasSubQuestion() == 1, answerPaperDetail.getStudentAnswer(), questionInfo.getQuestionAns(), subQuestionInfos);
                        }else{
                            // 根据题目Id查询小题
                            subQuestionInfos = subQuestionInfoDao.selectSubQuestionInfoListByQuestionInfoId(questionId);
                            // 创建客观题和主观题对象
                            objResultVO = new ObjResultVO(questionId, questionInfo.getDescription(), questionInfo.getQuestionScore(), 0, "", questionInfo.getQuestionAns(), optionInfos);
                            subResultVO = new SubResultVO(questionId, questionInfo.getDescription(), questionInfo.getQuestionScore(), 0, questionInfo.getHasSubQuestion() == 1, "", questionInfo.getQuestionAns(), subQuestionInfos);
                        }
                        // 根据题目类型放到指定的list
                        if (Objects.equals(questionInfo.getType(), "SINGLE")) {
                            singleList.add(objResultVO);
                        }
                        else if(Objects.equals(questionInfo.getType(), "MULTI")) {
                            multiList.add(objResultVO);
                        }
                        else if(Objects.equals(questionInfo.getType(), "JUDGE")) {
                            judgeList.add(objResultVO);
                        }
                        else if(Objects.equals(questionInfo.getType(), "COMPLETION")) {
                            completionList.add(objResultVO);
                        }
                        else if(Objects.equals(questionInfo.getType(), "COMPLEX")) {
                            complexList.add(subResultVO);
                        }
                    });
            examScoreDetailVO.setSingle(new SingleVO(singleList));
            examScoreDetailVO.setMulti(new MultiVO(multiList));
            examScoreDetailVO.setJudge(new JudgeVO(judgeList));
            examScoreDetailVO.setCompletion(new CompletionVO(completionList));
            examScoreDetailVO.setComplex(new ComplexVO(complexList));
            return examScoreDetailVO;
        }
    }
}

