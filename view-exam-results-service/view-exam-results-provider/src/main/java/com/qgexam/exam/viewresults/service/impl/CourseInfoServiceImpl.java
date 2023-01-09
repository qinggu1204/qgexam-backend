package com.qgexam.exam.viewresults.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.qgexam.exam.viewresults.dao.CourseInfoDao;
import com.qgexam.exam.viewresults.pojo.PO.CourseInfo;
import com.qgexam.exam.viewresults.service.CourseInfoService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 课程信息表(CourseInfo)表服务实现类
 *
 * @author ljy
 * @since 2022-1-6 19:42:52
 */
@DubboService
public class CourseInfoServiceImpl extends ServiceImpl<CourseInfoDao, CourseInfo> implements CourseInfoService {
    @Autowired
    private CourseInfoDao courseInfoDao;

    @Override
    public Integer getFinalScore(Integer courseId, Integer studentId) {
        return courseInfoDao.getFinalScore(courseId,studentId);
    }
}

