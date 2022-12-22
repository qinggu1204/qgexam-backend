package com.qgexam.user.service.impl;

import cn.dev33.satoken.session.SaSession;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qgexam.common.core.constants.SystemConstants;
import com.qgexam.common.core.utils.BeanCopyUtils;
import com.qgexam.user.dao.CourseInfoDao;
import com.qgexam.user.dao.TeacherInfoDao;
import com.qgexam.user.dao.UserInfoDao;
import com.qgexam.user.pojo.PO.*;
import com.qgexam.user.pojo.VO.GetCourseListVO;
import com.qgexam.user.pojo.VO.UserInfoVO;
import com.qgexam.user.service.CourseInfoService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * 课程信息表(CourseInfo)表服务实现类
 *
 * @author tageshi
 * @since 2022-12-16 19:42:52
 */
@DubboService
public class CourseInfoServiceImpl extends ServiceImpl<CourseInfoDao, CourseInfo> implements CourseInfoService {
    @Autowired
    private TeacherInfoDao teacherInfoDao;
    @Autowired
    private CourseInfoDao courseInfoDao;
    @Autowired
    private UserInfoDao userInfoDao;
    @Override
    public IPage<GetCourseListVO> getCourseList(SaSession session, Integer subjectId,
                                                String year, String semester,Integer currentPage,Integer pageSize) {
        IPage<GetCourseListVO> page=new Page<>(currentPage,pageSize);
        List<GetCourseListVO >getCourseListVO = new ArrayList<>();
        /*获取role*/
        UserInfoVO userInfoVO = (UserInfoVO) session.get(SystemConstants.SESSION_USER_KEY);
        List<RoleInfo> roleInfoList=userInfoDao.selectRoleInfoListById(userInfoVO.getUserInfo().getUserId());
        String role=new String();
        /*优先考虑教务教师身份*/
        for (RoleInfo roleInfo:roleInfoList) {
            role=roleInfo.getRoleName();
            if(role.equals("neteacher")){
                break;
            }
        }
        /*根据不同角色，获取DTO中的筛选条件，完成筛选*/
        TeacherInfo teacherInfo = userInfoVO.getTeacherInfo();
        StudentInfo studentInfo = userInfoVO.getStudentInfo();
        switch (role){
            case "teacher":
                /*获取课程信息*/
                getCourseListVO=courseInfoDao.getCourseListByTeacher(teacherInfo.getTeacherId(),subjectId,year,semester);
                for (GetCourseListVO course:getCourseListVO) {
                    course.setTeacherList(courseInfoDao.getCourseTeacherList(course.getCourseId()));
                }
                break;
            case "student":
                /*获取课程信息*/
                getCourseListVO=courseInfoDao.getCourseListByStudent(studentInfo.getStudentId(),subjectId,year,semester);
                for (GetCourseListVO course:getCourseListVO) {
                    course.setTeacherList(courseInfoDao.getCourseTeacherList(course.getCourseId()));
                }
                break;
            case "neteacher":
                getCourseListVO=courseInfoDao.getCourseListByNeteacher(teacherInfo.getSchoolId(),subjectId,year,semester);
                for (GetCourseListVO course:getCourseListVO) {
                    course.setTeacherList(courseInfoDao.getCourseTeacherList(course.getCourseId()));
                }
                break;
            case "admin":
                getCourseListVO=courseInfoDao.getCourseListByAdmin(subjectId,year,semester);
                for (GetCourseListVO course:getCourseListVO) {
                    course.setTeacherList(courseInfoDao.getCourseTeacherList(course.getCourseId()));
                }
                break;
            default:
        }
        page.setRecords(getCourseListVO);
        return page.convert(courseInfo -> BeanCopyUtils.copyBean(courseInfo,GetCourseListVO.class));
    }
}

