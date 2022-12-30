package com.qgexam.user.service.impl;

import cn.dev33.satoken.session.SaSession;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qgexam.common.core.constants.SystemConstants;
import com.qgexam.common.core.utils.BeanCopyUtils;
import com.qgexam.user.dao.*;
import com.qgexam.user.pojo.DTO.CreateExamDTO;
import com.qgexam.user.pojo.PO.*;
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
    @Autowired
    private CourseInfoDao courseInfoDao;
    @Autowired
    private MessageInfoDao messageInfoDao;

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
        /*1.创建新考试对象*/
        ExaminationInfo examinationInfo = new ExaminationInfo();
        examinationInfo.setExaminationPaperId(createExamDTO.getExaminationPaperId());
        examinationInfo.setExaminationName(createExamDTO.getExaminationName());
        examinationInfo.setCreatedBy(userId);
        examinationInfo.setStartTime(createExamDTO.getStartTime());
        examinationInfo.setEndTime(createExamDTO.getEndTime());
        examinationInfo.setLimitTime(createExamDTO.getLimitTime());
        examinationInfo.setIsQuestionResort(createExamDTO.getIsQuestionResort());
        examinationInfo.setIsOptionResort(createExamDTO.getIsOptionResort());
        /*2.向数据表插入考试并获取该考试的examinationId*/
        if (examinationInfoDao.insertExaminationInfo(examinationInfo) == 0) { return false; }
        Integer examinationId = examinationInfo.getExaminationId();
        /*3.根据学科编号获取课程列表*/
        List<CourseInfo> courseList=courseInfoDao.getCourseListBySubject(createExamDTO.getSubjectId());
        /*4.给这些课程及其学生发布考试*/
        for (CourseInfo course:courseList) {
            /*4.1.插入课程考试关联表*/
            if (teacherInfoDao.insertCourseExamination(examinationId,course.getCourseId()) == 0) {
                return false;
            }
            /*4.2.获取该课程的学生列表*/
            List<StudentInfo> studentInfoList=teacherInfoDao.getStudentListByCourse(course.getCourseId());
            /*4.3.给学生发布考试通知*/
            for(StudentInfo student:studentInfoList){
                MessageInfo messageInfo = new MessageInfo();
                messageInfo.setUserId(student.getUserId());
                messageInfo.setTitle("考试通知");
                messageInfo.setExaminationName(createExamDTO.getExaminationName());
                messageInfo.setStartTime(createExamDTO.getStartTime());
                messageInfo.setEndTime(createExamDTO.getEndTime());
                if(messageInfoDao.insertMessageInfo(messageInfo) == 0){
                    return false;
                }
            }
        }
        return true;
    }
}
