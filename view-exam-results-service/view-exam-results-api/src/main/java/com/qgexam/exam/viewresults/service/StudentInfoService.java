package com.qgexam.exam.viewresults.service;

import cn.dev33.satoken.session.SaSession;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qgexam.user.pojo.PO.StudentInfo;
import com.qgexam.user.pojo.VO.GetStudentInfoVO;

/**
 * 学生表(StudentInfo)表服务接口
 *
 * @author peter guo
 * @since 2022-12-15 14:28:41
 */
public interface StudentInfoService extends IService<StudentInfo> {

    GetStudentInfoVO getStudentInfo(SaSession session);
    Boolean updateStudentInfo(Integer userId,String loginName,String headImg,String faceImg);
    boolean joinCourse(Integer studentId, String userName, String studentNumber, Integer courseId);
}

