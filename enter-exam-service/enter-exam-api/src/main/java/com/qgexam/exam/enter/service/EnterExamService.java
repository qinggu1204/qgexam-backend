package com.qgexam.exam.enter.service;

import com.qgexam.exam.enter.pojo.VO.GetExaminationInfoVO;
import com.qgexam.exam.enter.pojo.VO.GetExaminationPaperVO;
import org.apache.dubbo.config.annotation.DubboService;

public interface EnterExamService {
    GetExaminationPaperVO getExaminationPaper(Integer examinationId);

    GetExaminationInfoVO getExaminationInfo(Integer examinationId);

}
