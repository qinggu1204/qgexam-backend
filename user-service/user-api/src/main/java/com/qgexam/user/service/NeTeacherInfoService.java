package com.qgexam.user.service;

import cn.dev33.satoken.session.SaSession;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qgexam.user.pojo.DTO.CreateExamDTO;
import com.qgexam.user.pojo.DTO.GetInvigilationInfoDTO;
import com.qgexam.user.pojo.VO.*;

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
}
