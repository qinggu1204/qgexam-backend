package com.qgexam.exam.viewresults.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qgexam.common.core.constants.ExamConstants;
import com.qgexam.common.core.utils.BeanCopyUtils;
import com.qgexam.exam.viewresults.dao.*;
import com.qgexam.exam.viewresults.pojo.PO.*;
import com.qgexam.exam.viewresults.pojo.VO.*;
import com.qgexam.exam.viewresults.service.AnswerPaperInfoService;
import com.qgexam.user.pojo.VO.QuestionInfoVO;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 答卷表(AnswerPaperInfo)表服务实现类
 *
 * @author ljy
 * @since 2023-01-10 16:35:22
 */
@DubboService
public class AnswerPaperInfoServiceImpl extends ServiceImpl<AnswerPaperInfoDao, AnswerPaperInfo> implements AnswerPaperInfoService {
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
    @Transactional
    public ExamScoreDetailVO getExamScoreDetail(Integer examinationId, Integer studentId){
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
        // 创建考试成绩明细对象
        ExamScoreDetailVO examScoreDetailVO = new ExamScoreDetailVO();
        examScoreDetailVO.setTotalScore(examinationPaper.getTotalScore());
        examScoreDetailVO.setStuTotalScore(answerPaperInfo.getPaperTotalScore());
        // 获取题目list
        List<QuestionInfo> questionInfoList = examinationPaper.getQuestionInfoList();
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

