package com.qgexam.user.service.impl;

import cn.dev33.satoken.session.SaSession;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qgexam.common.core.api.AppHttpCodeEnum;
import com.qgexam.common.core.constants.ExamConstants;
import com.qgexam.common.core.constants.SystemConstants;
import com.qgexam.common.core.exception.BusinessException;
import com.qgexam.common.core.utils.BeanCopyUtils;
import com.qgexam.user.dao.CourseInfoDao;
import com.qgexam.user.dao.StudentExamActionDao;
import com.qgexam.user.dao.StudentInfoDao;
import com.qgexam.user.dao.UserInfoDao;
import com.qgexam.user.pojo.PO.StudentExamAction;
import com.qgexam.user.pojo.PO.StudentInfo;
import com.qgexam.user.pojo.PO.UserInfo;
import com.qgexam.user.pojo.VO.GetStudentInfoVO;
import com.qgexam.user.pojo.VO.UserInfoVO;
import com.qgexam.user.service.StudentInfoService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 学生表(StudentInfo)表服务实现类
 *
 * @author peter guo
 * @since 2022-12-15 14:29:30
 */
@DubboService
public class StudentInfoServiceImpl extends ServiceImpl<StudentInfoDao, StudentInfo> implements StudentInfoService {

    @Autowired
    private StudentInfoDao studentInfoDao;

    @Autowired
    private UserInfoDao userInfoDao;
    @Autowired
    private CourseInfoDao courseInfoDao;

    @Autowired
    private StudentExamActionDao studentExamActionDao;

    @Override
    public GetStudentInfoVO getStudentInfo(SaSession session) {
        // 获取session中的用户信息
        UserInfoVO userInfoVO = (UserInfoVO) session.get(SystemConstants.SESSION_USER_KEY);
        //获取用户信息
        UserInfo userInfo = userInfoVO.getUserInfo();
        //获取学生信息
        StudentInfo studentInfo = userInfoVO.getStudentInfo();
        GetStudentInfoVO getStudentInfoVO = BeanCopyUtils.copyFromManyBean(GetStudentInfoVO.class, userInfo, studentInfo);
        return getStudentInfoVO;
    }

    /**
     * 修改学生信息
     *
     * @author ljy
     * @since 2022-12-15714:29:30
     */
    @Override
    public Boolean updateStudentInfo(Integer userId,String loginName,String headImg,String faceImg) {
        if (userInfoDao.updateLoginNameAndHeadImgByUserId(loginName,headImg,userId) != 0 && studentInfoDao.updatefaceImgByUserId(faceImg,userId) != 0) {
            return true;
        }
        return false;
    }

    /**
     * 学生加入课程
     *
     * @author ljy
     * @since 2022-12-17 14:29:30
     */
    @Override
    public boolean joinCourse(Integer studentId, String userName, String studentNumber, Integer courseId) {
        // 判断改课程是否存在
        if (courseInfoDao.getCourseInfoById(courseId) == null) {
            throw new BusinessException(AppHttpCodeEnum.SYSTEM_ERROR.getCode(), "该课程不存在");
        }
        // 判断该学生是否已经加入该课程
        if (studentInfoDao.getCountByStudentIdAndCourseId(studentId, courseId) != 0) {
            throw new BusinessException(AppHttpCodeEnum.SYSTEM_ERROR.getCode(), "该学生已经加入该课程");
        }
        if(courseInfoDao.selectById(courseId) == null){
            throw new BusinessException(AppHttpCodeEnum.SYSTEM_ERROR.getCode(), "课程已结束");
        }else return studentInfoDao.joinCourse(studentId, userName, studentNumber, courseId) != 0;
    }
    @Override
    public Integer getStudentId(SaSession session){
        UserInfoVO userInfoVO = (UserInfoVO) session.get(SystemConstants.SESSION_USER_KEY);
        //获取用户信息
        UserInfo userInfo = userInfoVO.getUserInfo();
        //获取学生信息
        StudentInfo studentInfo = userInfoVO.getStudentInfo();
        return studentInfo.getStudentId();
    }

    @Override
    public Integer getFaceErrorNumber(Integer examinationId, Integer studentId) {
        LambdaQueryWrapper<StudentExamAction> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StudentExamAction::getExaminationId, examinationId)
                .eq(StudentExamAction::getStudentId, studentId)
                .eq(StudentExamAction::getType, ExamConstants.FACE_ACTION);
        Long count = studentExamActionDao.selectCount(queryWrapper);
        return count.intValue();
    }
}

