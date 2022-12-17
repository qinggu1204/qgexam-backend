package com.qgexam.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.dev33.satoken.session.SaSession;
import com.baomidou.mybatisplus.extension.service.IService;

import com.qgexam.user.pojo.DTO.CreateCourseDTO;

import com.qgexam.user.pojo.PO.TeacherInfo;
import com.qgexam.user.pojo.VO.StudentVO;
import com.qgexam.user.pojo.VO.GetTeacherInfoVO;

/**
 * 教师表(TeacherInfo)表服务接口
 * @author yzw
 * @author tageshi
 * @since 2022-12-16 17:28:21
 */
public interface TeacherInfoService extends IService<TeacherInfo> {



    void createCourse(Integer teacherId, String userName, CreateCourseDTO createCourseDTO);


    IPage<StudentVO> getStudentList(Integer courseId, Integer currentPage, Integer pageSize);

    GetTeacherInfoVO getTeacherInfo(SaSession session);

}

