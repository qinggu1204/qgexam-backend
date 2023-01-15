package com.qgexam.exam.viewresults.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qgexam.exam.viewresults.pojo.PO.ErrorquestionInfo;
import com.qgexam.exam.viewresults.pojo.VO.ErrorQuestionListVO;

/**
 * 错题信息表(ErrorquestionInfo)表服务接口
 *
 * @author ljy
 * @since 2023-01-15 15:09:01
 */
public interface ErrorquestionInfoService extends IService<ErrorquestionInfo> {
    ErrorQuestionListVO getErrorQuestionList(Integer courseId, Integer studentId);
}

