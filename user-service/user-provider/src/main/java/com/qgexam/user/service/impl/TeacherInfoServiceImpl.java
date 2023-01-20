package com.qgexam.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cn.dev33.satoken.session.SaSession;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qgexam.common.core.api.AppHttpCodeEnum;
import com.qgexam.common.core.constants.RoleConstants;
import com.qgexam.common.core.exception.BusinessException;
import com.qgexam.common.core.utils.BeanCopyUtils;
import com.qgexam.common.core.constants.SystemConstants;
import com.qgexam.user.dao.CourseInfoDao;
import com.qgexam.user.dao.ExaminationInfoDao;
import com.qgexam.user.dao.TeacherInfoDao;
import com.qgexam.user.pojo.DTO.CourseTeacherDTO;
import com.qgexam.user.pojo.DTO.CreateCourseDTO;
import com.qgexam.user.pojo.DTO.GetExamListDTO;
import com.qgexam.user.pojo.PO.*;
import com.qgexam.user.pojo.VO.*;
import com.qgexam.user.service.TeacherInfoService;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


/**
 * 教师表(TeacherInfo)表服务实现类
 *
 * @author yzw
 * @since 2022-12-16 16:00:15
 */
@DubboService
@Transactional
public class TeacherInfoServiceImpl extends ServiceImpl<TeacherInfoDao, TeacherInfo> implements TeacherInfoService {

    @Autowired
    private TeacherInfoDao teacherInfoDao;

    @Autowired
    private CourseInfoDao courseInfoDao;

    @Autowired
    private ExaminationInfoDao examinationInfoDao;

    @Override
    public IPage<StudentVO> getStudentList(Integer courseId, Integer currentPage, Integer pageSize) {
        IPage<StudentVO> page = new Page<>(currentPage, pageSize);
        return teacherInfoDao.getStudentPage(courseId, page);
    }

    @Override
    public GetTeacherInfoVO getTeacherInfo(SaSession session) {
        /*获取用户信息*/
        UserInfoVO userInfoVO = (UserInfoVO) session.get(SystemConstants.SESSION_USER_KEY);
        UserInfo userInfo = userInfoVO.getUserInfo();
        /*获取教师信息*/
        TeacherInfo teacherInfo = userInfoVO.getTeacherInfo();
        GetTeacherInfoVO getTeacherInfoVO = BeanCopyUtils.copyFromManyBean(GetTeacherInfoVO.class, userInfo, teacherInfo);
        return getTeacherInfoVO;
    }

    /**
     * 教师向课程表插入记录，同时向课程-教师表插入记录
     *
     * @param teacherId
     * @param userName
     * @param createCourseDTO
     * @author yzw
     */
    @Override
    @Transactional
    public void createCourse(Integer teacherId, String userName, CreateCourseDTO createCourseDTO) {
        CourseInfo courseInfo = new CourseInfo();
        courseInfo.setSubjectId(createCourseDTO.getSubjectId());
        courseInfo.setSubjectName(createCourseDTO.getSubjectName());
        courseInfo.setCourseName(createCourseDTO.getCourseName());
        courseInfo.setCourseUrl(createCourseDTO.getCourseUrl());
        courseInfo.setYear(createCourseDTO.getYearName());
        courseInfo.setSemester(createCourseDTO.getSemesterName());
        courseInfoDao.insertCourseInfo(courseInfo);
        CourseTeacherDTO courseTeacherDTO = new CourseTeacherDTO();
        courseTeacherDTO.setCourseId(courseInfo.getCourseId());
        courseTeacherDTO.setTeacherId(teacherId);
        courseTeacherDTO.setUserName(userName);
        Integer count = courseInfoDao.insertCourseTeacher(courseTeacherDTO);

        if (count < 1) {
            throw BusinessException.newInstance(AppHttpCodeEnum.SYSTEM_ERROR);
        }


    }

    @Override
    public IPage<ScoreVO> getScoreList(Integer courseId, Integer currentPage, Integer pageSize) {
        IPage<ScoreVO> page = new Page<>(currentPage, pageSize);
        return teacherInfoDao.getScorePage(courseId, page);
    }

    @Override
    public IPage<ExaminationVO> getExamList(GetExamListDTO getExamListDTO) {
        UserInfoVO userInfoVO = getExamListDTO.getUserInfoVO();
        Integer schoolId = userInfoVO.getTeacherInfo().getSchoolId();

        UserInfo userInfo = userInfoVO.getUserInfo();
        List<RoleInfo> roleList = userInfo.getRoleList();

        IPage<ExaminationInfo> page = new Page<>(getExamListDTO.getCurrentPage(), getExamListDTO.getPageSize());

        // 判断是否为教务教师
        for (RoleInfo roleInfo : roleList) {
            if (RoleConstants.ROLE_NETEACHER.equals(roleInfo.getRoleName())) {
                List<GetCourseListVO> courseList = courseInfoDao.getCourseListByNeteacher(schoolId, null, null, null);
                List<Integer> courseIdList = courseList.stream()
                        .map(GetCourseListVO::getCourseId).collect(Collectors.toList());
                // 查询课程的所有考试
                IPage<ExaminationVO> pageVO = examinationInfoDao.selectAllExaminationInfo(page, courseIdList);
                return pageVO;
            }
        }

        List<GetCourseListVO> courseList = courseInfoDao.getCourseListByTeacher(getExamListDTO.getTeacherId(), getExamListDTO.getCourseId(),
                null, null);
        List<Integer> courseIdList = courseList.stream().map(GetCourseListVO::getCourseId).collect(Collectors.toList());
        IPage<ExaminationVO> pageVO = examinationInfoDao.selectAllExaminationInfo(page, courseIdList);
        return pageVO;

    }
}

