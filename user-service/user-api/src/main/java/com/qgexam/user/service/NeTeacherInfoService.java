package com.qgexam.user.service;

import cn.dev33.satoken.session.SaSession;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qgexam.user.pojo.DTO.CreateExamDTO;
import com.qgexam.user.pojo.DTO.GetInvigilationInfoDTO;
import com.qgexam.user.pojo.PO.CourseInfo;
import com.qgexam.user.pojo.VO.ChapterInfoListVO;
import com.qgexam.user.pojo.VO.GetCourseListVO;
import com.qgexam.user.pojo.VO.GetExaminationPaperVO;
import com.qgexam.user.pojo.VO.GetInvigilationInfoVO;

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
}
