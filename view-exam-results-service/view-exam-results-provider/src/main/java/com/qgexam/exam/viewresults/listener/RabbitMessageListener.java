package com.qgexam.exam.viewresults.listener;

import com.qgexam.common.core.constants.ExamConstants;
import com.qgexam.common.redis.utils.RedisCache;
import com.qgexam.exam.viewresults.dao.*;
import com.qgexam.exam.viewresults.pojo.DTO.ErrorDTO;
import com.qgexam.exam.viewresults.pojo.DTO.ErrorQuestionDTO;
import com.qgexam.exam.viewresults.pojo.PO.*;
import com.qgexam.exam.viewresults.pojo.VO.*;
import com.qgexam.rabbit.constants.RabbitMQConstants;

import com.qgexam.rabbit.constants.ViewExamResultsRabbitConstant;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author ljy
 * @date 2023年01月10日 13:54
 */
@Slf4j
@Component
public class RabbitMessageListener {
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private ExaminationInfoDao examinationInfoDao;
    @Autowired
    private ExaminationPaperDao examinationPaperDao;
    @Autowired
    private CourseInfoDao courseInfoDao;
    @Autowired
    private AnswerPaperInfoDao answerPaperInfoDao;
    @Autowired
    private AnswerPaperDetailDao answerPaperDetailDao;
    @Autowired
    private OptionInfoDao optionInfoDao;
    @Autowired
    private SubQuestionInfoDao subQuestionInfoDao;
    @Autowired
    private QuestionInfoDao questionInfoDao;
    @Autowired
    private ErrorquestionInfoDao errorquestionInfoDao;
    @Autowired
    private SubErrorquestionInfoDao subErrorquestionInfoDao;

    /**
     * 查询成绩开放前将某场考试的所有学生的成绩明细存入缓存（监听阅卷结束发送的消息）
     * @author ljy
     * @date 2023/1/10 15:59
     */
    @RabbitListener(queues = RabbitMQConstants.BEGIN_CACHE_QUEUE_NAME)
    public void listenBeginCacheQueue(Integer examinationId, Channel channel, Message message) throws IOException {
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
        // 获取题目list
        List<QuestionInfo> questionInfoList = examinationPaper.getQuestionInfoList();
        try {
            // 将查询成绩时间存入redis并设置缓存时间
            redisCache.setCacheObject(ExamConstants.EXAMRESULT_QUERYTIME_HASH_KEY_PREFIX + examinationId, examinationInfo.getResultQueryTime(), 2, TimeUnit.MINUTES);
            // 将试卷总分存入redis
            redisCache.setCacheObject(ExamConstants.EXAMRESULT_TOTALSCORE_HASH_KEY_PREFIX + examinationId, examinationPaper.getTotalScore(), 2, TimeUnit.MINUTES);
            // 根据考试编号获取课程列表
            List<Integer> courseList = courseInfoDao.getCourseIdListByExaminationId(examinationId);
            // 把这些课程的学生考试成绩明细放入缓存
            for (Integer courseId : courseList) {
                // 获取该课程的学生列表
                List<Integer> studentInfoList = courseInfoDao.getStudentIdListByCourseId(courseId);
                // 把该课程的学生考试成绩明细放入缓存
                for (Integer studentId : studentInfoList) {
                    // 查询该学生的答卷
                    AnswerPaperInfo answerPaperInfo = answerPaperInfoDao.selectStuAnswerPaper(examinationId, studentId);
                    // 将答卷总分存入redis
                    redisCache.setCacheObject(ExamConstants.EXAMRESULT_STUTOTALSCORE_HASH_KEY_PREFIX + examinationId + ExamConstants.EXAMRESULT_STUID_HASH_KEY_PREFIX + studentId, answerPaperInfo.getPaperTotalScore(), 2, TimeUnit.MINUTES);
                    // 创建不同题型的list
                    List<ObjResultVO> singleList = new ArrayList<ObjResultVO>();
                    List<ObjResultVO> multiList = new ArrayList<ObjResultVO>();
                    List<ObjResultVO> judgeList = new ArrayList<ObjResultVO>();
                    List<ObjResultVO> completionList = new ArrayList<ObjResultVO>();
                    List<SubResultVO> complexList = new ArrayList<SubResultVO>();
                    questionInfoList.stream()
                            .forEach(questionInfo -> {
                                // 获取题目Id
                                Integer questionId = questionInfo.getQuestionId();
                                // 根据题目Id和答卷Id查询学生的答卷明细表
                                AnswerPaperDetail answerPaperDetail = answerPaperDetailDao.selectStuAnswerDetail(answerPaperInfo.getAnswerPaperId(), questionId);
                                // 根据题目Id查询选项
                                List<OptionResultVO> optionInfos = optionInfoDao.selectOptionInfoListByQuestionInfoId(questionId);
                                // 创建小题明细对象
                                List<SubQuestionResultVO> subQuestionInfos;
                                // 创建客观题明细对象
                                ObjResultVO objResultVO;
                                // 创建主观题明细对象
                                SubResultVO subResultVO;
                                // 判断是否有答卷明细表
                                if(answerPaperDetail != null) {
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
                                if (Objects.equals(questionInfo.getType(), "SINGLE")) {
                                    singleList.add(objResultVO);
                                } else if (Objects.equals(questionInfo.getType(), "MULTI")) {
                                    multiList.add(objResultVO);
                                } else if (Objects.equals(questionInfo.getType(), "JUDGE")) {
                                    judgeList.add(objResultVO);
                                } else if (Objects.equals(questionInfo.getType(), "COMPLETION")) {
                                    completionList.add(objResultVO);
                                } else if (Objects.equals(questionInfo.getType(), "COMPLEX")) {
                                    complexList.add(subResultVO);
                                }
                            });
                    // 将题目信息放入map中，方便下一步存入redis
                    // list转换为map
                    Map<String, ObjResultVO> singleMap = singleList.stream()
                            .collect(Collectors.toMap(
                                    objResultVO -> objResultVO.getQuestionId().toString(),
                                    objResultVO -> objResultVO
                            ));

                    Map<String, ObjResultVO> multiMap = multiList.stream()
                            .collect(Collectors.toMap(
                                    objResultVO -> objResultVO.getQuestionId().toString(),
                                    objResultVO -> objResultVO
                            ));
                    Map<String, ObjResultVO> judgeMap = judgeList.stream()
                            .collect(Collectors.toMap(
                                    objResultVO -> objResultVO.getQuestionId().toString(),
                                    objResultVO -> objResultVO
                            ));

                    Map<String, ObjResultVO> completionMap = completionList.stream()
                            .collect(Collectors.toMap(
                                    objResultVO -> objResultVO.getQuestionId().toString(),
                                    objResultVO -> objResultVO
                            ));

                    Map<String, SubResultVO> complexMap = complexList.stream()
                            .collect(Collectors.toMap(
                                    subResultVO -> subResultVO.getQuestionId().toString(),
                                    subResultVO -> subResultVO
                            ));
                    // 将题目信息存入redis
                    // 单选题
                    redisCache.setCacheMap(ExamConstants.EXAMRESULT_SINGLE_QUESTION_HASH_FIELD + examinationId + ExamConstants.EXAMRESULT_STUID_HASH_KEY_PREFIX + studentId, singleMap);
                    // 设置超时时间
                    redisCache.expire(ExamConstants.EXAMRESULT_SINGLE_QUESTION_HASH_FIELD + examinationId + ExamConstants.EXAMRESULT_STUID_HASH_KEY_PREFIX + studentId, 2, TimeUnit.MINUTES);

                    redisCache.setCacheMap(ExamConstants.EXAMRESULT_MULTI_QUESTION_HASH_FIELD + examinationId + ExamConstants.EXAMRESULT_STUID_HASH_KEY_PREFIX + studentId, multiMap);
                    redisCache.expire(ExamConstants.EXAMRESULT_MULTI_QUESTION_HASH_FIELD + examinationId + ExamConstants.EXAMRESULT_STUID_HASH_KEY_PREFIX + studentId, 2, TimeUnit.MINUTES);


                    redisCache.setCacheMap(ExamConstants.EXAMRESULT_JUDGE_QUESTION_HASH_FIELD + examinationId + ExamConstants.EXAMRESULT_STUID_HASH_KEY_PREFIX + studentId, judgeMap);
                    redisCache.expire(ExamConstants.EXAMRESULT_JUDGE_QUESTION_HASH_FIELD + examinationId + ExamConstants.EXAMRESULT_STUID_HASH_KEY_PREFIX + studentId, 2, TimeUnit.MINUTES);

                    redisCache.setCacheMap(ExamConstants.EXAMRESULT_COMPLETION_QUESTION_HASH_FIELD + examinationId + ExamConstants.EXAMRESULT_STUID_HASH_KEY_PREFIX + studentId, completionMap);
                    redisCache.expire(ExamConstants.EXAMRESULT_COMPLETION_QUESTION_HASH_FIELD + examinationId + ExamConstants.EXAMRESULT_STUID_HASH_KEY_PREFIX + studentId, 2, TimeUnit.MINUTES);

                    redisCache.setCacheMap(ExamConstants.EXAMRESULT_COMPLEX_QUESTION_HASH_FIELD + examinationId + ExamConstants.EXAMRESULT_STUID_HASH_KEY_PREFIX + studentId, complexMap);
                    redisCache.expire(ExamConstants.EXAMRESULT_COMPLEX_QUESTION_HASH_FIELD + examinationId + ExamConstants.EXAMRESULT_STUID_HASH_KEY_PREFIX + studentId, 2, TimeUnit.MINUTES);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            log.info("考试{}的所有学生的答卷存入缓存失败", examinationId);
            // 手动reject
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
            return;
        }
        log.info("考试{}的所有学生的答卷存入缓存成功", examinationId);
        // 手动ack
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

    /**
     * 把学生的错题加入错题集（监听加入错题集接口发送的消息）
     * @author ljy
     * @date 2023/1/14 15:59
     */
    @Transactional(rollbackFor = Exception.class)
    @RabbitListener(queues = ViewExamResultsRabbitConstant.EXAM_RVIEWRESULTS_QUEUE_NAME)
    public void listenExamViewResultsQueue(ErrorQuestionDTO errorQuestionDTO, Channel channel, Message message) throws IOException {
        // 设置插入语句是否成功的标志
        Integer flag = 0;
        // 根据考试Id查询考试信息
        ExaminationInfo examinationInfo = examinationInfoDao.selectById(errorQuestionDTO.getExaminationId());
        // 如果examinationInfo为空，抛出BusinessException
        if (examinationInfo == null) {
            throw new RuntimeException("考试不存在");
        }
        // 根据examinationInfo.paperId查询试卷信息
        Integer examinationPaperId = examinationInfo.getExaminationPaperId();
        QuestionInfo questionInfo = questionInfoDao.selectQuestionInfoById(errorQuestionDTO.getQuestionId(), examinationPaperId);
        ErrorquestionInfo errorquestionInfo = new ErrorquestionInfo();
        errorquestionInfo.setStudentId(errorQuestionDTO.getStudentId());
        errorquestionInfo.setQuestionId(errorQuestionDTO.getQuestionId());
        errorquestionInfo.setSubjectId(questionInfo.getSubjectId());
        errorquestionInfo.setType(questionInfo.getType());
        errorquestionInfo.setDescription(questionInfo.getDescription());
        errorquestionInfo.setChapterName(questionInfo.getChapterName());
        errorquestionInfo.setDifficultyLevel(questionInfo.getDifficultyLevel());
        errorquestionInfo.setQuestionScore(questionInfo.getQuestionScore());
        errorquestionInfo.setQuestionAns(questionInfo.getQuestionAns());
        errorquestionInfo.setHasSubQuestion(questionInfo.getHasSubQuestion());
        AnswerPaperInfo answerPaperInfo = answerPaperInfoDao.selectStuAnswerPaper(errorQuestionDTO.getExaminationId(), errorQuestionDTO.getStudentId());
        AnswerPaperDetail answerPaperDetail = answerPaperDetailDao.selectStuAnswerDetail(answerPaperInfo.getAnswerPaperId(), errorQuestionDTO.getQuestionId());
        try {
            List<SubQuestionResultVO> subQuestionInfos;
            // 判断是否有答卷明细表，没有的话题目得分设置为0，学生答案设置为""
            if (answerPaperDetail != null) {
                errorquestionInfo.setScore(answerPaperDetail.getScore());
                errorquestionInfo.setStudentAnswer(answerPaperDetail.getStudentAnswer());
                subQuestionInfos = subQuestionInfoDao.selectSubQuestionInfoList(answerPaperDetail.getAnswerPaperDetailId(), errorQuestionDTO.getQuestionId());
            } else {
                errorquestionInfo.setScore(0);
                errorquestionInfo.setStudentAnswer("");
                subQuestionInfos = subQuestionInfoDao.selectSubQuestionInfoListByQuestionInfoId(errorQuestionDTO.getQuestionId());
            }
            if (errorquestionInfo.getHasSubQuestion() == 1) {
                for (SubQuestionResultVO subQuestionResultVO : subQuestionInfos) {
                    SubErrorquestionInfo subErrorquestionInfo = new SubErrorquestionInfo();
                    subErrorquestionInfo.setStudentId(errorQuestionDTO.getStudentId());
                    subErrorquestionInfo.setQuestionId(errorQuestionDTO.getQuestionId());
                    subErrorquestionInfo.setSubQuestionId(subQuestionResultVO.getSubQuestionId());
                    subErrorquestionInfo.setSubQuestionDesc(subQuestionResultVO.getSubQuestionDesc());
                    subErrorquestionInfo.setSubQuestionAns(subQuestionResultVO.getSubQuestionAns());
                    subErrorquestionInfo.setSubQuestionAnswer(subQuestionResultVO.getSubQuestionAnswer());
                    if (subErrorquestionInfoDao.insert(subErrorquestionInfo) == 0) {
                        throw new RuntimeException("错题小题信息表插入失败");
                    }
                }
            }
            if (errorquestionInfoDao.insert(errorquestionInfo) == 0) {
                throw new RuntimeException("错题表插入失败");
            }
        }catch (Exception e){
            e.printStackTrace();
            log.info("学生{}的错题{}添加失败", errorQuestionDTO.getStudentId(),errorQuestionDTO.getQuestionId());
            // 手动reject
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
            return;
        }
        log.info("学生{}的错题{}添加成功", errorQuestionDTO.getStudentId(),errorQuestionDTO.getQuestionId());
        // 手动ack
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}
