package com.qgexam.exam.finish.service.impl;

import com.qgexam.exam.finish.dao.*;
import com.qgexam.exam.finish.pojo.DTO.QuestionDTO;
import com.qgexam.exam.finish.pojo.DTO.SaveOrSubmitDTO;
import com.qgexam.exam.finish.pojo.DTO.SubQuestionDTO;
import com.qgexam.exam.finish.service.FinishExamService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
    private ExamSubmitRecordDao examSubmitRecordDao;
    @Override
    @Transactional
    public boolean saveOrSubmit(SaveOrSubmitDTO saveOrSubmitDTO,Integer studentId) {
        Integer examinationId=saveOrSubmitDTO.getExaminationId();
        boolean flag=true;
        /*插入提交记录*/
        SimpleDateFormat now= new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String submitTime=now.toString();
        if(examSubmitRecordDao.insertRecord(studentId,examinationId,submitTime)==0){
            flag=false;
        }
        /*获取学生答卷和考试试卷*/
        Integer answerPaperId=answerPaperInfoDao.getAnswerPaperId(studentId,examinationId);
        Integer examinationPaperId=examinationInfoDao.getExaminationPaperId(examinationId);
        /*定义临时变量*/
        Integer questionId;
        Integer subQuestionId;
        String questionAnswer;
        String subQuestionAnswer;
        /*提取body中的题目array*/
        List<QuestionDTO>questionDTO=saveOrSubmitDTO.getQuestion();
        for (QuestionDTO question:questionDTO) {
            questionId=question.getQuestionId();
            questionAnswer=question.getQuestionAnswer();
            String correctAnswer=questionInfoDao.getCorrectAnswer(questionId);
            Integer questionScore=examinationPaperQuestionDao.getScore(examinationPaperId,questionId);
            switch (questionInfoDao.geyTypeByQuestionId(questionId)){
                /*客观题直接判分*/
                case "SINGLE":
                case "JUDGE":
                case "MULTI":
                    if(questionAnswer.equals(correctAnswer)){
                        if(answerPaperDetailDao.insertAnswerPaperDetail(answerPaperId,questionId,questionAnswer,questionScore)==0){
                            flag=false;
                        }
                    }
                    else {
                        if(answerPaperDetailDao.insertAnswerPaperDetail(answerPaperId,questionId,questionAnswer,0)==0){
                            flag=false;
                        }
                    }
                    break;
                case "COMPLETION":
                case "COMPLEX":
                    if(answerPaperDetailDao.insertAnswerPaperDetail(answerPaperId,questionId,questionAnswer,0)==0){
                        flag=false;
                    }
                    /*判断是否有小题，以填写questionAnswer的值*/
                    if(questionInfoDao.hasSubQuestion(questionId)!=0){
                        /*提取题目的小题array*/
                        List<SubQuestionDTO>subQuestionDTO=question.getSubQuestion();
                        for (SubQuestionDTO subQuestion:subQuestionDTO) {
                            subQuestionId=subQuestion.getSubQuestionId();
                            subQuestionAnswer=subQuestion.getSubQuestionAnswer();
                            if( subQuestionAnswerDetailDao.insert(answerPaperDetailDao.getAnswerPaperDetailId(answerPaperId,questionId),subQuestionId,subQuestionAnswer)==0){
                                flag=false;
                            }
                        }
                    }
                    break;
                default:break;
            }
        }
        if(flag){
            return true;
        }
        else return false;
    }
}
