package com.qgexam.user.service;

import cn.dev33.satoken.session.SaSession;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qgexam.user.pojo.DTO.CreateExamDTO;
import com.qgexam.user.pojo.DTO.GetInvigilationInfoDTO;
import com.qgexam.user.pojo.VO.ChapterInfoListVO;
import com.qgexam.user.pojo.VO.GetExaminationPaperVO;
import com.qgexam.user.pojo.VO.GetInvigilationInfoVO;
import com.qgexam.user.pojo.DTO.CreatePaperDTO;
import com.qgexam.user.pojo.VO.SubjectVO;
import com.qgexam.user.pojo.VO.GetTeacherInfoVO;
import com.qgexam.user.pojo.VO.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @author yzw
 * @description
 * @date 2022/12/21 20:00:44
 */
public interface NeTeacherInfoService{
    IPage<GetExaminationPaperVO> getExaminationPaperList(SaSession session, Integer currentPage, Integer pageSize);
    boolean arrangeInvigilation(Integer examinationId);
    List<ChapterInfoListVO> getChapterInfoList(Integer subjectId);
    boolean createExamination(Integer userId, CreateExamDTO createExamDTO);

    IPage<GetInvigilationInfoVO> getInvigilationInfo(GetInvigilationInfoDTO getInvigilationInfoDTO);

    List<PreviewQuestionInfoVO> previewPaper(Integer examinationPaperId);

    void createPaper(Integer userId, CreatePaperDTO createPaperDTO);

    List<SubjectVO> getSubjectList();

    void distributeJudgeTask(Integer examinationId, LocalDateTime endTime);

}
