package com.qgexam.exam.enter.service;

import com.qgexam.exam.enter.pojo.DTO.JoinExamDTO;
import com.qgexam.exam.enter.pojo.VO.GetExaminationInfoVO;
import com.qgexam.exam.enter.pojo.VO.GetExaminationPaperVO;

public interface EnterExamService {
    GetExaminationPaperVO getExaminationPaper(JoinExamDTO joinExamDTO);

    GetExaminationInfoVO getExaminationInfo(JoinExamDTO joinExamDTO);

    void screenCutting(JoinExamDTO joinExamDTO);

    Integer getScreenCuttingNumber(JoinExamDTO joinExamDTO);

    Boolean checkExam(JoinExamDTO joinExamDTO);
}
