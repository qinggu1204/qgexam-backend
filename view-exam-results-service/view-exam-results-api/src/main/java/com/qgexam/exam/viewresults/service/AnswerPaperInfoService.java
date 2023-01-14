package com.qgexam.exam.viewresults.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qgexam.exam.viewresults.pojo.PO.AnswerPaperInfo;
import com.qgexam.exam.viewresults.pojo.VO.ExamScoreDetailVO;

/**
 * 答卷表(AnswerPaperInfo)表服务接口
 *
 * @author ljy
 * @since 2023-01-10 16:34:27
 */
public interface AnswerPaperInfoService extends IService<AnswerPaperInfo> {
    ExamScoreDetailVO getExamScoreDetail(Integer examinationId, Integer studentId);
}

