package com.qgexam.user.service.impl;

import cn.dev33.satoken.session.SaSession;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qgexam.common.core.constants.SystemConstants;
import com.qgexam.common.core.utils.BeanCopyUtils;
import com.qgexam.user.dao.ChapterInfoDao;
import com.qgexam.user.dao.ExaminationInfoDao;
import com.qgexam.user.dao.TeacherInfoDao;
import com.qgexam.user.pojo.DTO.CreateExamDTO;
import com.qgexam.user.pojo.PO.CourseInfo;
import com.qgexam.user.pojo.PO.ExaminationInfo;
import com.qgexam.user.pojo.PO.TeacherInfo;
import com.qgexam.user.pojo.VO.ChapterInfoListVO;
import com.qgexam.user.pojo.VO.GetCourseListVO;
import com.qgexam.user.pojo.VO.GetExaminationPaperVO;
import com.qgexam.user.pojo.VO.UserInfoVO;
import com.qgexam.user.service.NeTeacherInfoService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author yzw
 * @description
 * @date 2022/12/21 20:01:03
 */
@DubboService
public class NeTeacherInfoServiceImpl implements NeTeacherInfoService {
    @Autowired
    private TeacherInfoDao teacherInfoDao;
    @Autowired
    private ExaminationInfoDao examinationInfoDao;
    @Autowired
    private ChapterInfoDao chapterInfoDao;

    /**
     * 查看试卷列表
     */
    @Override
    public IPage<GetExaminationPaperVO> getExaminationPaperList(SaSession session, Integer currentPage, Integer pageSize) {
        IPage<GetExaminationPaperVO> page=new Page<>(currentPage,pageSize);
        /*获取用户信息*/
        UserInfoVO userInfoVO = (UserInfoVO) session.get(SystemConstants.SESSION_USER_KEY);
        List<GetExaminationPaperVO> getExaminationPaperVOList=teacherInfoDao.getExaminationPaperList(userInfoVO.getUserInfo().getUserId());
        page.setRecords(getExaminationPaperVOList);
        return page.convert(examinationPaper -> BeanCopyUtils.copyBean(examinationPaper, GetExaminationPaperVO.class));
    }

    /**
     * 安排监考教师
     */
    @Override
    @Transactional
    public boolean arrangeInvigilation(Integer examinationId) {
        boolean flag=true;
        /*获取可以安排的教师列表*/
        List<TeacherInfo> availableTeacherList=teacherInfoDao.getInvigilationTeacherList(examinationId);
        /*获取参加该场考试的课程列表*/
        List<CourseInfo> availableCourseList=teacherInfoDao.getCourseExaminationList(examinationId);
        /*获取考试名*/
        String examinationName=examinationInfoDao.getByExaminationId(examinationId).getExaminationName();
        /*随机抽取列表中的教师作为监考教师*/
        Random rand=new Random();
        int randomIndex = rand.nextInt(availableTeacherList.size());
        for (CourseInfo course:availableCourseList) {
            for(int i=0;i<availableCourseList.size();i++){
                TeacherInfo randomElement = availableTeacherList.get(randomIndex);
                if(teacherInfoDao.arrangeInvigilation(examinationId,course.getCourseId(),randomElement.getTeacherId(),course.getCourseName(),randomElement.getUserName(),examinationName)==0){
                    flag=false;
                }
                availableTeacherList.remove(randomIndex);
            }
        }
        if(flag) return true;
        else return false;
    }

    /**
     * @author ljy
     * @description 获取学科章节列表
     * @date 2022/12/25 20:01:03
     */
    @Override
    public List<ChapterInfoListVO> getChapterInfoList(Integer subjectId){
        return chapterInfoDao.getChapterListBySubject(subjectId);
    }

    /**
     * @author ljy
     * @description 创建（发布）考试
     * @date 2022/12/28 22:01:03
     */
    @Override
    public boolean createExamination(Integer userId, CreateExamDTO createExamDTO){
        ExaminationInfo examinationInfo = new ExaminationInfo();
        examinationInfo.setExaminationPaperId(createExamDTO.getExaminationPaperId());
        examinationInfo.setExaminationName(createExamDTO.getExaminationName());
        examinationInfo.setCreatedBy(userId);
        examinationInfo.setStartTime(createExamDTO.getStartTime());
        examinationInfo.setEndTime(createExamDTO.getEndTime());
        examinationInfo.setLimitTime(createExamDTO.getLimitTime());
        examinationInfo.setIsQuestionResort(createExamDTO.getIsQuestionResort());
        examinationInfo.setIsOptionResort(createExamDTO.getIsOptionResort());
        if (examinationInfoDao.insertExaminationInfo(examinationInfo) != 0) {
            return true;
        }
        return false;
    }
}
