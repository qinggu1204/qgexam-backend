package com.qgexam.exam.finish.service;

import com.qgexam.exam.finish.pojo.DTO.SaveOrSubmitDTO;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.context.annotation.Bean;

/**
 * 答卷表(AnswerPaperInfo)表服务接口
 *
 * @author tageshi
 * @since 2023-01-09 19:14:55
 */
@DubboService
public interface FinishExamService{
    @Bean
    public boolean saveOrSubmit(SaveOrSubmitDTO saveOrSubmitDTO,Integer studentId);
}