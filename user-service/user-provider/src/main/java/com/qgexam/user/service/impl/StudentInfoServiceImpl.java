package com.qgexam.user.service.impl;

import cn.dev33.satoken.session.SaSession;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qgexam.common.core.constants.SystemConstants;
import com.qgexam.common.core.utils.BeanCopyUtils;
import com.qgexam.user.dao.StudentInfoDao;
import com.qgexam.user.dao.UserInfoDao;
import com.qgexam.user.pojo.PO.StudentInfo;
import com.qgexam.user.pojo.PO.UserInfo;
import com.qgexam.user.pojo.VO.GetStudentInfoVO;
import com.qgexam.user.pojo.VO.UserInfoVO;
import com.qgexam.user.service.StudentInfoService;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.config.annotation.Service;
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
        if (studentInfoDao.joinCourse(studentId,userName,studentNumber,courseId) != 0) {
            return true;
        }
        return false;
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
}

