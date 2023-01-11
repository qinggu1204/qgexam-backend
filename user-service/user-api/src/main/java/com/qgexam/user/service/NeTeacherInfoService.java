package com.qgexam.user.service;

import com.qgexam.user.pojo.DTO.CreatePaperDTO;
import com.qgexam.user.pojo.VO.SubjectVO;

import java.util.Date;
import java.util.List;

/**
 * @author yzw
 * @description
 * @date 2022/12/21 20:00:44
 */
public interface NeTeacherInfoService {
    void createPaper(Integer teacherId, CreatePaperDTO createPaperDTO);

    List<SubjectVO> getSubjectList();

    void distributeJudgeTask(Integer examinationId, Date endTime);

}
