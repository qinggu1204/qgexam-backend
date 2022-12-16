package com.qgexam.user.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qgexam.common.core.utils.BeanCopyUtils;
import com.qgexam.user.dao.TeacherInfoDao;
import com.qgexam.user.pojo.PO.StudentInfo;
import com.qgexam.user.pojo.PO.TeacherInfo;
import com.qgexam.user.pojo.VO.StudentVO;
import com.qgexam.user.service.TeacherInfoService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 教师表(TeacherInfo)表服务实现类
 *
 * @author yzw
 * @since 2022-12-16 16:00:15
 */
@Service
public class TeacherInfoServiceImpl extends ServiceImpl<TeacherInfoDao, TeacherInfo> implements TeacherInfoService {

    @Autowired
    private TeacherInfoDao teacherInfoDao;

    @Override
    public IPage<StudentVO> getStudentList(Integer courseId, Integer currentPage, Integer pageSize) {
        IPage<StudentInfo> page=new Page<>(currentPage,pageSize);
        IPage<StudentInfo> studentPage = teacherInfoDao.getStudentPage(courseId,page);
        //将studentInfo转化为VO并封装到分页对象中返回
        return studentPage.convert(studentInfo -> BeanCopyUtils.copyBean(studentInfo, StudentVO.class));
    }
}

