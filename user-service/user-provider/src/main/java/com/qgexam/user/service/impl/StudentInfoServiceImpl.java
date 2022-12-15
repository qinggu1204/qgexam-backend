package com.qgexam.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qgexam.user.dao.StudentInfoDao;
import com.qgexam.user.pojo.PO.StudentInfo;
import com.qgexam.user.service.StudentInfoService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 学生表(StudentInfo)表服务实现类
 *
 * @author peter guo
 * @since 2022-12-15 14:29:30
 */
@Service
public class StudentInfoServiceImpl extends ServiceImpl<StudentInfoDao, StudentInfo> implements StudentInfoService {

    @Autowired
    private StudentInfoDao studentInfoDao;

    @Override
    public StudentInfo getStudentInfoByUserId(Integer userId) {
        LambdaQueryWrapper<StudentInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StudentInfo::getUserId, userId);
        return studentInfoDao.selectOne(queryWrapper);
    }
}

