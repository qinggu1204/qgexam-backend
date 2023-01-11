package com.qgexam.marking.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qgexam.marking.pojo.DTO.MarkingDTO;
import com.qgexam.marking.pojo.VO.AnswerPaperVO;
import com.qgexam.marking.pojo.VO.GetAnswerPaperVO;
import com.qgexam.marking.pojo.VO.TaskVO;

import java.util.List;

/**
 * @description 教师服务实现类
 * @author peter guo
 * @date 2022/12/28 20:29:05
 */
public interface MarkingService {
    IPage<TaskVO> getTaskList(Integer teacherId,Integer currentPage, Integer pageSize);

    IPage<AnswerPaperVO> getAnswerPaperList(Integer teacherId, Integer examinationId, Integer currentPage, Integer pageSize);

    List<GetAnswerPaperVO> getAnswerPaper(Integer answerPaperId);

    void marking(Integer answerPaperId, List<MarkingDTO> questionList);
}
