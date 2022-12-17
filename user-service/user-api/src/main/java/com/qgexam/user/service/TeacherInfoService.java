package com.qgexam.user.service;

import cn.dev33.satoken.session.SaSession;
import com.baomidou.mybatisplus.extension.service.IService;

import com.qgexam.user.pojo.DTO.CreateCourseDTO;
import com.qgexam.user.pojo.DTO.UpdateTeacherInfoDTO;

import com.qgexam.user.pojo.PO.TeacherInfo;
import com.qgexam.user.pojo.VO.GetTeacherInfoVO;

/**
 * 教师表(TeacherInfo)表服务接口
 * @author yzw
 * @author tageshi
 * @since 2022-12-16 17:28:21
 */
public interface TeacherInfoService extends IService<TeacherInfo> {



    void createCourse(Integer teacherId, String userName, CreateCourseDTO createCourseDTO);

    GetTeacherInfoVO getTeacherInfo(SaSession session);

}

