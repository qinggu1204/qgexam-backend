package com.qgexam.exam.viewresults.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qgexam.exam.viewresults.dao.ErrorquestionInfoDao;
import com.qgexam.exam.viewresults.pojo.PO.ErrorquestionInfo;
import com.qgexam.exam.viewresults.pojo.VO.ErrorQuestionListVO;
import com.qgexam.exam.viewresults.service.ErrorquestionInfoService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 错题信息表(ErrorquestionInfo)表服务实现类
 *
 * @author ljy
 * @since 2023-01-15 15:09:20
 */
@DubboService
public class ErrorquestionInfoServiceImpl extends ServiceImpl<ErrorquestionInfoDao, ErrorquestionInfo> implements ErrorquestionInfoService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ErrorQuestionListVO getErrorQuestionList(Integer courseId, Integer studentId){
        return new ErrorQuestionListVO();
    }
}

