package com.qgexam.user.service;

import cn.dev33.satoken.session.SaSession;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qgexam.user.pojo.PO.CourseInfo;
import com.qgexam.user.pojo.VO.GetCourseListVO;

import java.util.List;

/**
 * 课程信息表(CourseInfo)表服务接口
 *
 * @author tageshi
 * @since 2022-12-16 19:37:09
 */
public interface CourseInfoService extends IService<CourseInfo> {
    IPage<GetCourseListVO> getCourseList(SaSession session, Integer courseId, String year, String semester,Integer currentPage,Integer pageSize);

}

