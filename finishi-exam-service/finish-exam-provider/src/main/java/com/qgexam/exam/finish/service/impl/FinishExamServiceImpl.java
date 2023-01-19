package com.qgexam.exam.finish.service.impl;

import com.qgexam.common.core.constants.ExamConstants;
import com.qgexam.common.redis.utils.RedisCache;
import com.qgexam.exam.finish.dao.*;
import com.qgexam.exam.finish.pojo.DTO.QuestionDTO;
import com.qgexam.exam.finish.pojo.DTO.SaveOrSubmitDTO;
import com.qgexam.exam.finish.pojo.DTO.SubQuestionDTO;
import com.qgexam.exam.finish.pojo.PO.ExaminationInfo;
import com.qgexam.exam.finish.service.FinishExamService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.qgexam.common.core.constants.ExamConstants.EXAMINATIONANSWER_DETAIL_KEY_PREFIX;

/**
 * @author tageshi
 * @date 2023/1/9 19:27
 */
@DubboService
public class FinishExamServiceImpl implements FinishExamService {
    @Autowired
    private QuestionInfoDao questionInfoDao;
    @Autowired
    private AnswerPaperInfoDao answerPaperInfoDao;
    @Autowired
    private AnswerPaperDetailDao answerPaperDetailDao;
    @Autowired
    private ExaminationInfoDao examinationInfoDao;
    @Autowired
    private ExaminationPaperQuestionDao examinationPaperQuestionDao;
    @Autowired
    private SubQuestionAnswerDetailDao subQuestionAnswerDetailDao;
    @Autowired
    private RedisCache redisCache;

    /**
     * 学生提交作答
     */
    @Override
    @Transactional
    public boolean saveOrSubmit(SaveOrSubmitDTO saveOrSubmitDTO,Integer studentId) {
        boolean flag=true;
        /*获取考试信息*/
        Integer examinationId=saveOrSubmitDTO.getExaminationId();
        /*获取学生答卷和考试试卷*/
        Integer answerPaperId=answerPaperInfoDao.getAnswerPaperId(studentId,examinationId);
        Integer examinationPaperId=examinationInfoDao.getExaminationPaperId(examinationId);
        /*定义临时变量*/
        Integer questionId;
        Integer subQuestionId;
        String questionAnswer;
        String subQuestionAnswer;
        Integer objectiveScore=0;
        Integer answerPaperDetailId;
        /*提取body中的题目array*/
        List<QuestionDTO>questionDTO=saveOrSubmitDTO.getQuestion();
        for (QuestionDTO question:questionDTO) {
            questionId=question.getQuestionId();
            questionAnswer=question.getQuestionAnswer();
            Integer questionScore=examinationPaperQuestionDao.getScore(examinationPaperId,questionId);
            switch (questionInfoDao.geyTypeByQuestionId(questionId)){
                /*客观题直接判分*/
                case "MULTI":
                    String multiAns="";
                    for (int i=0;i<questionAnswer.length();i++){
                        if(questionAnswer.charAt(i)>='A'&&questionAnswer.charAt(i)<='D'){
                            multiAns+=questionAnswer.charAt(i);
                        }
                    }
                    char[] ans=multiAns.toCharArray();
                    Arrays.sort(ans);
                    questionAnswer=new String(ans);
                case "JUDGE":
                case "SINGLE":
                    String correctAnswer=questionInfoDao.getCorrectAnswer(questionId);
                    if(questionAnswer.equals(correctAnswer)){
                        if(answerPaperDetailDao.updateAnswerPaperDetail(answerPaperId,questionId,questionAnswer,questionScore)==0){
                            if(answerPaperDetailDao.insertAnswerPaperDetail(answerPaperId,questionId,questionAnswer,questionScore)==0){
                                flag=false;
                            }
                        }
                        objectiveScore+=questionScore;
                    }
                    else {
                        if(answerPaperDetailDao.updateAnswerPaperDetail(answerPaperId,questionId,questionAnswer,0)==0){
                            if(answerPaperDetailDao.insertAnswerPaperDetail(answerPaperId,questionId,questionAnswer,0)==0){
                                flag=false;
                            }
                        }
                    }
                    break;
                case "COMPLETION":
                case "COMPLEX":
                    if(answerPaperDetailDao.updateAnswerPaperDetail(answerPaperId,questionId,questionAnswer,0)==0){
                        if(answerPaperDetailDao.insertAnswerPaperDetail(answerPaperId,questionId,questionAnswer,0)==0){
                            flag=false;
                        }
                    }
                    /*判断是否有小题，以填写questionAnswer的值*/
                    if(questionInfoDao.hasSubQuestion(questionId)!=0){
                        /*提取题目的小题array*/
                        List<SubQuestionDTO>subQuestionDTO=question.getSubQuestion();
                        for (SubQuestionDTO subQuestion:subQuestionDTO) {
                            subQuestionId=subQuestion.getSubQuestionId();
                            subQuestionAnswer=subQuestion.getSubQuestionAnswer();
                            answerPaperDetailId=answerPaperDetailDao.getAnswerPaperDetailId(answerPaperId,questionId);
                            if(subQuestionAnswerDetailDao.update(answerPaperDetailId,subQuestionId,subQuestionAnswer)==0){
                                if(subQuestionAnswerDetailDao.insert(answerPaperDetailId,subQuestionId,subQuestionAnswer)==0){
                                    flag=false;
                                }
                            }
                        }
                    }
                    break;
                default:break;
            }
        }
        answerPaperInfoDao.insertObjectiveScore(answerPaperId,objectiveScore);
        if(flag){
            return true;
        }
        else return false;
    }

    @Override
    public LocalDateTime getEndTime(Integer examinationId) {
        com.qgexam.user.pojo.PO.ExaminationInfo examinationInfo = redisCache.getCacheObject(ExamConstants.EXAMINATION_INFO_HASH_KEY_PREFIX + examinationId);
        return examinationInfo.getEndTime();
    }

    /**
     * 学生保存作答（到Redis缓存）
     */
    @Override
    @Transactional
    public boolean save(SaveOrSubmitDTO saveOrSubmitDTO, Integer studentId){
        /*获取Redis前缀*/
        /*将提交信号试卷id和学生id放入缓存中*/
        String detailPrefix = EXAMINATIONANSWER_DETAIL_KEY_PREFIX+examinationInfoDao.getExaminationPaperId(saveOrSubmitDTO.getExaminationId())+":"+studentId;
        com.qgexam.user.pojo.PO.ExaminationInfo examinationInfo =
                redisCache.getCacheObject(ExamConstants.EXAMINATION_INFO_HASH_KEY_PREFIX
                        + saveOrSubmitDTO.getExaminationId());
        /*考试不在进行中时抛出异常*/
        if (examinationInfo == null) {
            throw new RuntimeException("考试不存在");
        }
        redisCache.setCacheObject(detailPrefix,saveOrSubmitDTO);
        return true;
    }
}
