package com.qgexam.user.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qgexam.user.dao.TeacherInfoDao;
import com.qgexam.user.pojo.VO.ChapterInfoListVO;
import com.qgexam.user.pojo.VO.GetNeteacherListVO;
import com.qgexam.user.pojo.VO.GetTeacherListVO;
import com.qgexam.user.pojo.VO.StudentVO;
import com.qgexam.user.service.AdminInfoService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

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
     * @description 管理员查看任课教师列表
     * @date 2022/1/7 20:01:03
     */
    @Override
    public IPage<GetTeacherListVO> getTeacherList(Integer currentPage, Integer pageSize, Integer schoolId, String loginName){
        IPage<GetTeacherListVO> page=new Page<>(currentPage,pageSize);
        //return teacherInfoDao.getStudentPage(courseId,page);
        return page;
    }

    /**
     * @author ljy
     * @description 管理员查看教务教师列表
     * @date 2022/1/7 20:01:03
     */
    @Override
    public IPage<GetNeteacherListVO> getNeteacherList(Integer currentPage, Integer pageSize, Integer schoolId, String loginName){
        IPage<GetNeteacherListVO> page=new Page<>(currentPage,pageSize);
        //return teacherInfoDao.getStudentPage(courseId,page);
        return page;
    }
}
