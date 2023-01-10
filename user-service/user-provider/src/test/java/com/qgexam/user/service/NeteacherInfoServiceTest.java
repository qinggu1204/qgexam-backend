package com.qgexam.user.service;

import com.qgexam.user.dao.*;
import com.qgexam.user.pojo.PO.AnswerPaperInfo;
import com.qgexam.user.pojo.PO.CourseInfo;
import com.qgexam.user.pojo.PO.MessageInfo;
import com.qgexam.user.pojo.PO.StudentInfo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @description 教务教师接口测试
 * @author ljy
 * @date 2022/1/7 19:20:04
 */

@SpringBootTest
public class NeteacherInfoServiceTest {

    @Autowired
    private CourseInfoDao courseInfoDao;
    @Autowired
    private TeacherInfoDao teacherInfoDao;
    @Autowired
    private AnswerPaperInfoDao answerPaperInfoDao;

    @Test
    public void getMessagecodeTest() {
        Integer examinationId = 4;
        /*3.根据学科编号获取课程列表*/
        List<CourseInfo> courseList = courseInfoDao.getCourseListBySubject(2);
        /*4.给这些课程及其学生发布考试*/
        for (CourseInfo course : courseList) {
            /*4.2.获取该课程的学生列表*/
            List<StudentInfo> studentInfoList = teacherInfoDao.getStudentListByCourse(course.getCourseId());
            /*4.3.给学生发布考试通知*/
            for (StudentInfo student : studentInfoList) {
                AnswerPaperInfo answerPaperInfo = new AnswerPaperInfo();
                answerPaperInfo.setStudentId(student.getStudentId());
                answerPaperInfo.setExaminationId(examinationId);
                answerPaperInfo.setExaminationName("2021-2022学年党的理论知识期末考");
                answerPaperInfoDao.insert(answerPaperInfo);
            }
        }
    }

}
