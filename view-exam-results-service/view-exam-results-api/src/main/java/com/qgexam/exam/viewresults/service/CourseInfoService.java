package com.qgexam.exam.viewresults.service;

import cn.dev33.satoken.session.SaSession;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qgexam.exam.viewresults.pojo.PO.CourseInfo;
import com.qgexam.user.pojo.VO.GetCourseListVO;

/**
 * 课程信息表(CourseInfo)表服务接口
 *
 * @author ljy
 * @since 2022-1-6 19:37:09
 */
public interface CourseInfoService extends IService<CourseInfo> {
    Integer getFinalScore(Integer courseId, Integer studentId);
}

