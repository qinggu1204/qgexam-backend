package com.qgexam.exam.finish.service;

import com.qgexam.exam.finish.pojo.DTO.SaveOrSubmitDTO;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.Date;

public interface FinishExamService{
    //提交
    public boolean saveOrSubmit(SaveOrSubmitDTO saveOrSubmitDTO,Integer studentId);
    public LocalDateTime getEndTime(Integer examinationId);
    //保存
    public boolean save(SaveOrSubmitDTO saveOrSubmitDTO,Integer studentId);
}