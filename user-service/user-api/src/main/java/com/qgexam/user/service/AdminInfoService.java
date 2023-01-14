package com.qgexam.user.service;

import cn.dev33.satoken.session.SaSession;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qgexam.user.pojo.VO.GetCourseListVO;
import com.qgexam.user.pojo.VO.GetStudentVO;
import com.qgexam.user.pojo.VO.GetTeacherListVO;

/**
 * @author ljy
 * @description 管理员服务接口
 * @date 2022/1/6 20:00:44
 */
public interface AdminInfoService {
    boolean addNeteacher(Integer userId);
    IPage<GetTeacherListVO> getTeacherList(Integer currentPage, Integer pageSize, Integer schoolId, Integer roleId, String loginName);
    IPage<GetStudentVO> getStudentList(Integer schoolId, String loginName, Integer currentPage, Integer pageSize);
    boolean updateStudentNumber(Integer studentId,String newStudentNumber);
    boolean updateTeacherNumber(Integer teacherId,String newTeacherNumber);
}
