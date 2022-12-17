package com.qgexam.user.service.impl;

import cn.dev33.satoken.session.SaSession;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qgexam.common.core.constants.SystemConstants;
import com.qgexam.common.core.utils.BeanCopyUtils;
import com.qgexam.user.dao.CourseInfoDao;
import com.qgexam.user.dao.TeacherInfoDao;
import com.qgexam.user.dao.UserRoleInfoDao;
import com.qgexam.user.pojo.PO.CourseInfo;
import com.qgexam.user.pojo.PO.TeacherInfo;
import com.qgexam.user.pojo.PO.UserInfo;
import com.qgexam.user.pojo.VO.GetCourseListVO;
import com.qgexam.user.pojo.VO.UserInfoVO;
import com.qgexam.user.service.CourseInfoService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * 课程信息表(CourseInfo)表服务实现类
 *
 * @author tageshi
 * @since 2022-12-16 19:42:52
 */
@Service
public class CourseInfoServiceImpl extends ServiceImpl<CourseInfoDao, CourseInfo> implements CourseInfoService {

    @Autowired
    private UserRoleInfoDao userRoleInfoDao;
    @Autowired
    private TeacherInfoDao teacherInfoDao;
    @Autowired
    private CourseInfoDao courseInfoDao;
   /* @Override
    public List<GetCourseListVO> getCourseList(SaSession session, Integer subjectId, String year, String semester) {
        List courseList=new ArrayList<>();
        GetCourseListVO GetCourseListVO=new GetCourseListVO();
        CourseInfo courseInfo;
        *//*获取role*//*
        UserInfo userInfo=(UserInfo) session.get(SystemConstants.SESSION_USER_KEY);
        String role = userRoleInfoDao.getRoleNameByUserId(userInfo.getUserId());
        *//*根据不同角色，获取DTO中的筛选条件，完成筛选*//*
        switch (role){
            case "teacher":
                *//*获取教师id用于查询该教师所教授的课程*//*
                Integer teacherId=teacherInfoDao.getTeacherInfoByUserId(userInfo.getUserId()).getTeacherId();
                *//*循环查找*//*
                *//*获取课程信息*//*
                while (courseInfoDao.getCourseListByTeacher(teacherId,subjectId,year,semester)!=null){
//                    courseInfo=courseInfoDao.getCourseListByTeacher(teacherId,subjectId,year,semester);
//                    GetCourseListVO.setTeacherList(courseInfoDao.getCourseTeacherList(courseInfo.getCourseId()));
//                    GetCourseListVO.setCourseUrl(courseInfo.getCourseUrl());
//                    GetCourseListVO.setCourseName(courseInfo.getCourseName());
//                    GetCourseListVO.setSemester(courseInfo.getSemester());
//                    GetCourseListVO.setYear(courseInfo.getYear());
//                    GetCourseListVO.setIsDeleted(courseInfo.getIsDeleted());
//                    courseList.add(GetCourseListVO);
                }
                break;
            case "student":
                break;
            case "neteacher":
                break;
            case "admin":
                break;
            default:
        }

        return courseList;
    }*/


    @Override
    public IPage<GetCourseListVO> getCourseList(SaSession session, Integer subjectId,
                                                String year, String semester,Integer currentPage,Integer pageSize) {
        IPage<CourseInfo> page=new Page<>(currentPage,pageSize);
        UserInfoVO userInfoVO = (UserInfoVO) session.get(SystemConstants.SESSION_USER_KEY);
        TeacherInfo teacherInfo = userInfoVO.getTeacherInfo();
        Integer teacherId = teacherInfo.getTeacherId();
        courseInfoDao.getCourseListByTeacher(teacherId,subjectId,year,semester,page);
        return page.convert(courseInfo -> BeanCopyUtils.copyBean(courseInfo,GetCourseListVO.class));
    }
}

