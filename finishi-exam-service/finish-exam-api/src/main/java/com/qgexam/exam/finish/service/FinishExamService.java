package com.qgexam.exam.finish.service;

import com.qgexam.exam.finish.pojo.DTO.SaveOrSubmitDTO;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.context.annotation.Bean;

public interface FinishExamService{
    public boolean saveOrSubmit(SaveOrSubmitDTO saveOrSubmitDTO,Integer studentId);
}