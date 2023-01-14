package com.qgexam.user.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qgexam.common.core.utils.BeanCopyUtils;
import com.qgexam.user.dao.StudentInfoDao;
import com.qgexam.user.dao.TeacherInfoDao;
import com.qgexam.user.pojo.PO.StudentInfo;
import com.qgexam.user.pojo.VO.GetCourseListVO;
import com.qgexam.user.pojo.VO.GetStudentVO;
import com.qgexam.user.pojo.VO.GetTeacherListVO;
import com.qgexam.user.service.AdminInfoService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ljy
 * @description 管理员服务接口
 * @date 2022/1/6 20:00:44
 */
@DubboService
public class AdminInfoServiceImpl implements AdminInfoService {
    @Autowired
    private TeacherInfoDao teacherInfoDao;
    @Autowired
    private StudentInfoDao studentInfoDao;
    /**
     * @author ljy
     * @description 设置教务老师
     * @date 2022/1/7 20:01:03
     */
    @Override
    public boolean addNeteacher(Integer userId){
        if (teacherInfoDao.insertNeteacher(3,"neteacher",userId) != 0) {
            return true;
        }
        return false;
    }

    /**
     * @author ljy
     * @description 管理员查看任课/教务教师列表
     * @date 2022/1/7 20:01:03
     */
    @Override
    public IPage<GetTeacherListVO> getTeacherList(Integer currentPage, Integer pageSize, Integer schoolId, Integer roleId, String loginName){
        IPage<GetTeacherListVO> page=new Page<>(currentPage,pageSize);
        return teacherInfoDao.getTeacherPage(schoolId,roleId,loginName,page);
    }

    @Override
    public IPage<GetStudentVO> getStudentList(Integer schoolId, String loginName, Integer currentPage, Integer pageSize) {
        IPage<GetStudentVO> page=new Page<>(currentPage,pageSize);
        return studentInfoDao.getStudentList(schoolId,loginName,page);
    }

    @Override
    public boolean updateStudentNumber(Integer studentId, String newStudentNumber) {
        if(studentInfoDao.updateStudentNumber(studentId,newStudentNumber)!=0&&studentInfoDao.updateStudentNumberInStudentCourse(studentId,newStudentNumber)!=0){
            return true;
        }
        return false;
    }

    @Override
    public boolean updateTeacherNumber(Integer teacherId, String newTeacherNumber) {
       if(teacherInfoDao.updateTeacherNumberInteger(teacherId,newTeacherNumber)!=0){
           return true;
       }
       return false;
    }

}
