package com.qgexam.user.service.impl;

import cn.dev33.satoken.session.SaSession;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.qgexam.common.core.api.AppHttpCodeEnum;
import com.qgexam.common.core.exception.BusinessException;
import com.qgexam.user.dao.CourseInfoDao;
import com.qgexam.user.dao.TeacherInfoDao;
import com.qgexam.user.pojo.DTO.CreateCourseDTO;
import com.qgexam.user.pojo.DTO.UpdateTeacherInfoDTO;
import com.qgexam.user.pojo.PO.CourseInfo;

import com.qgexam.common.core.constants.SystemConstants;
import com.qgexam.common.core.utils.BeanCopyUtils;
import com.qgexam.user.dao.TeacherInfoDao;

import com.qgexam.user.pojo.PO.TeacherInfo;
import com.qgexam.user.pojo.PO.UserInfo;
import com.qgexam.user.pojo.VO.GetTeacherInfoVO;
import com.qgexam.user.pojo.VO.UserInfoVO;
import com.qgexam.user.service.TeacherInfoService;
import org.apache.dubbo.config.annotation.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


/**
 * 教师表(TeacherInfo)表服务实现类
 *
 * @author tageshi
 * @since 2022-12-16 17:29:35
 */
@Service
@Transactional
public class TeacherInfoServiceImpl extends ServiceImpl<TeacherInfoDao, TeacherInfo> implements TeacherInfoService {


    @Autowired
    private CourseInfoDao courseInfoDao;

    /**
     * 教师向课程表插入记录，同时向课程-教师表插入记录
     *
     * @param teacherId
     * @param userName
     * @param createCourseDTO
     * @author yzw
     */
    @Override
    @Transactional
    public void createCourse(Integer teacherId, String userName, CreateCourseDTO createCourseDTO) {
        CourseInfo courseInfo = new CourseInfo();
        courseInfo.setSubjectId(createCourseDTO.getSubjectId());
        courseInfo.setSubjectName(createCourseDTO.getSubjectName());
        courseInfo.setCourseName(createCourseDTO.getCourseName());
        courseInfo.setCourseUrl(createCourseDTO.getCourseUrl());
        courseInfo.setYear(createCourseDTO.getYearName());
        courseInfo.setSemester(createCourseDTO.getSemesterName());
        courseInfoDao.insertCourseInfo(courseInfo);

        Integer count = courseInfoDao.insertCourseTeacher(courseInfo.getCourseId(), teacherId, userName);

        if (count < 1) {
            throw BusinessException.newInstance(AppHttpCodeEnum.SYSTEM_ERROR);
        }


    }

    @Override
    public GetTeacherInfoVO getTeacherInfo(SaSession session) {
        /*获取用户信息*/
        UserInfoVO userInfoVO = (UserInfoVO) session.get(SystemConstants.SESSION_USER_KEY);
        UserInfo userInfo = userInfoVO.getUserInfo();
        /*获取教师信息*/
        TeacherInfo teacherInfo = userInfoVO.getTeacherInfo();
        GetTeacherInfoVO getTeacherInfoVO = BeanCopyUtils.copyFromManyBean(GetTeacherInfoVO.class, userInfo, teacherInfo);
        return getTeacherInfoVO;
    }
}

