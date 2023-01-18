package com.qgexam.exam.viewresults.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qgexam.exam.viewresults.dao.ErrorquestionInfoDao;
import com.qgexam.exam.viewresults.dao.OptionInfoDao;
import com.qgexam.exam.viewresults.dao.SubErrorquestionInfoDao;
import com.qgexam.exam.viewresults.pojo.PO.ErrorquestionInfo;
import com.qgexam.exam.viewresults.pojo.VO.*;
import com.qgexam.exam.viewresults.service.ErrorquestionInfoService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 错题信息表(ErrorquestionInfo)表服务实现类
 *
 * @author ljy
 * @since 2023-01-15 15:09:20
 */
@DubboService
public class ErrorquestionInfoServiceImpl extends ServiceImpl<ErrorquestionInfoDao, ErrorquestionInfo> implements ErrorquestionInfoService {
    @Autowired
    private ErrorquestionInfoDao errorquestionInfoDao;
    @Autowired
    private SubErrorquestionInfoDao subErrorquestionInfoDao;
    @Autowired
    private OptionInfoDao optionInfoDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ErrorQuestionListVO getErrorQuestionList(Integer courseId, Integer studentId){
        // 创建错题集对象
        ErrorQuestionListVO errorQuestionListVO = new ErrorQuestionListVO();
        // 获取错题list
        List<ErrorquestionInfo> errorquestionInfoList = errorquestionInfoDao.selectErrorquestionInfoList(courseId,studentId);
        // 创建不同题型的list
        List<ErrorObjResultVO> singleList = new ArrayList<>();
        List<ErrorObjResultVO> multiList = new ArrayList<>();
        List<ErrorObjResultVO> judgeList = new ArrayList<>();
        List<ErrorObjResultVO> completionList = new ArrayList<>();
        List<ErrorSubResultVO> complexList = new ArrayList<>();
        for(ErrorquestionInfo errorquestionInfo : errorquestionInfoList){
            // 根据题目Id查询选项
            List<OptionResultVO> optionInfos = optionInfoDao.selectOptionInfoListByQuestionInfoId(errorquestionInfo.getQuestionId());
            List<SubQuestionResultVO> subQuestionInfos = subErrorquestionInfoDao.selectSubQuestionInfoList(errorquestionInfo.getQuestionId(),studentId);
            // 创建客观题和主观题对象
            ErrorObjResultVO errorObjResultVO = new ErrorObjResultVO(errorquestionInfo.getQuestionId(),errorquestionInfo.getDescription(),errorquestionInfo.getChapterName(),errorquestionInfo.getDifficultyLevel(),errorquestionInfo.getQuestionScore(),errorquestionInfo.getScore(),errorquestionInfo.getStudentAnswer(),errorquestionInfo.getQuestionAns(),optionInfos);
            ErrorSubResultVO errorSubResultVO = new ErrorSubResultVO(errorquestionInfo.getQuestionId(),errorquestionInfo.getDescription(),errorquestionInfo.getChapterName(),errorquestionInfo.getDifficultyLevel(),errorquestionInfo.getQuestionScore(),errorquestionInfo.getScore(),errorquestionInfo.getHasSubQuestion() == 1,errorquestionInfo.getStudentAnswer(),errorquestionInfo.getQuestionAns(),subQuestionInfos);
            // 根据题目类型放到指定的list
            if (Objects.equals(errorquestionInfo.getType(), "SINGLE")) {
                singleList.add(errorObjResultVO);
            }
            else if(Objects.equals(errorquestionInfo.getType(), "MULTI")) {
                multiList.add(errorObjResultVO);
            }
            else if(Objects.equals(errorquestionInfo.getType(), "JUDGE")) {
                judgeList.add(errorObjResultVO);
            }
            else if(Objects.equals(errorquestionInfo.getType(), "COMPLETION")) {
                completionList.add(errorObjResultVO);
            }
            else if(Objects.equals(errorquestionInfo.getType(), "COMPLEX")) {
                complexList.add(errorSubResultVO);
            }
        }
        errorQuestionListVO.setSingle(new ErrorSingleVO(singleList));
        errorQuestionListVO.setMulti(new ErrorMultiVO(multiList));
        errorQuestionListVO.setJudge(new ErrorJudgeVO(judgeList));
        errorQuestionListVO.setCompletion(new ErrorCompletionVO(completionList));
        errorQuestionListVO.setComplex(new ErrorComplexVO(complexList));
        return errorQuestionListVO;
    }
}

