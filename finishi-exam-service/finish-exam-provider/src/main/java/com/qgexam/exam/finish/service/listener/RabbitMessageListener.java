package com.qgexam.exam.finish.service.listener;

import com.qgexam.exam.finish.dao.*;
import com.qgexam.exam.finish.pojo.DTO.QuestionDTO;
import com.qgexam.exam.finish.pojo.DTO.SaveOrSubmitDTO;
import com.qgexam.exam.finish.pojo.DTO.SubQuestionDTO;
import com.qgexam.rabbit.constants.FinishExamRabbitConstants;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Slf4j
public class RabbitMessageListener {
    @Autowired
    private ExaminationInfoDao examinationInfoDao;
    @Autowired
    private QuestionInfoDao questionInfoDao;
    @Autowired
    private AnswerPaperInfoDao answerPaperInfoDao;
    @Autowired
    private AnswerPaperDetailDao answerPaperDetailDao;
    @Autowired
    private ExaminationPaperQuestionDao examinationPaperQuestionDao;
    @Autowired
    private SubQuestionAnswerDetailDao subQuestionAnswerDetailDao;
    @Autowired
    private ExamSubmitRecordDao examSubmitRecordDao;
    @RabbitListener(queues = FinishExamRabbitConstants.EXAM_FINISH_NAME)
    @Transactional
    public void listenExamRecordQueue(SaveOrSubmitDTO saveOrSubmitDTO,Integer studentId,Channel channel, Message message)throws IOException{
        //判断提交时间是否早于考试时间
        Date submit=new Date();
        Date endTime=examinationInfoDao.getEndTimeDate(saveOrSubmitDTO.getExaminationId());
        Integer cmp=submit.compareTo(endTime);
        if(cmp<=0){
            Integer examinationId=saveOrSubmitDTO.getExaminationId();
            boolean flag=true;
            /*插入提交记录*/
            Date submitTime=new Date();
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
            Integer objectiveScore=0;
            /*提取body中的题目array*/
            List<QuestionDTO> questionDTO=saveOrSubmitDTO.getQuestion();
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
                                objectiveScore+=questionScore;
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
            answerPaperInfoDao.insertObjectiveScore(answerPaperId,objectiveScore);

            if(flag){
                log.info("已保存作答情况");
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            }
            else{
                log.info("保存失败");
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
            }
        }
        else {
            log.info("保存失败");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
        }
    }
}