package com.qgexam.user.service.impl;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qgexam.common.core.api.AppHttpCodeEnum;
import com.qgexam.common.core.constants.SystemConstants;
import com.qgexam.common.core.exception.BusinessException;
import com.qgexam.common.core.utils.BeanCopyUtils;
import com.qgexam.common.redis.utils.RedisCache;
import com.qgexam.user.dao.SchoolInfoDao;
import com.qgexam.user.dao.StudentInfoDao;
import com.qgexam.user.dao.TeacherInfoDao;
import com.qgexam.user.dao.UserInfoDao;
import com.qgexam.user.pojo.DTO.UserLoginByUsernameDTO;
import com.qgexam.user.pojo.PO.RoleInfo;
import com.qgexam.user.pojo.PO.StudentInfo;
import com.qgexam.user.pojo.PO.TeacherInfo;
import com.qgexam.user.pojo.PO.UserInfo;
import com.qgexam.user.pojo.VO.GetUserInfoVO;
import com.qgexam.user.pojo.VO.UserInfoVO;
import com.qgexam.user.service.UserInfoService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户信息表(UserInfo)表服务实现类
 *
 * @author lamb007
 * @since 2022-12-10 20:13:04
 */
@Service
@Transactional
public class UserInfoServiceImpl extends ServiceImpl<UserInfoDao, UserInfo> implements UserInfoService {

    @Autowired
    private UserInfoDao userInfoDao;

    @Autowired
    private TeacherInfoDao teacherInfoDao;

    @Autowired
    private StudentInfoDao studentInfoDao;

    @Autowired
    private RedisCache redisCache;


    /**
     * 用户名密码登录
     * @author yzw
     * @param loginDTO
     * @return
     */
    @Override
    public UserInfoVO userLogin(UserLoginByUsernameDTO loginDTO) {
        String loginName = loginDTO.getLoginName();
        String password = loginDTO.getPassword();

        UserInfo userInfo = userInfoDao.selectUserInfoByLoginName(loginName);
        if (userInfo == null) {
            throw BusinessException.newInstance(AppHttpCodeEnum.LOGIN_ERROR);
        }
        if (!userInfo.getPassword().equals(password)) {
            throw BusinessException.newInstance(AppHttpCodeEnum.LOGIN_ERROR);
        }
        Integer userId = userInfo.getUserId();

        // 根据userId获取学生信息
        LambdaQueryWrapper<StudentInfo> studentQueryWrapper = new LambdaQueryWrapper<>();
        studentQueryWrapper.eq(StudentInfo::getUserId, userId);
        StudentInfo studentInfo = studentInfoDao.selectOne(studentQueryWrapper);

        // 如果不是学生，就获取教师信息
        TeacherInfo teacherInfo = null;
        if (studentInfo == null) {
            LambdaQueryWrapper<TeacherInfo> teacherQueryWrapper = new LambdaQueryWrapper<>();
            teacherQueryWrapper.eq(TeacherInfo::getUserId, userId);
            teacherInfo = teacherInfoDao.selectOne(teacherQueryWrapper);
        }
        UserInfoVO userInfoVO = new UserInfoVO();
        userInfoVO.setUserInfo(userInfo);
        userInfoVO.setStudentInfo(studentInfo);
        userInfoVO.setTeacherInfo(teacherInfo);
        return userInfoVO;
    }

    @Override
    public List<String> getRoleListByUserId(Integer id) {
        List<RoleInfo> roleInfos = userInfoDao.selectRoleInfoListById(id);
        return roleInfos.stream()
                .map(RoleInfo::getRoleName)
                .collect(Collectors.toList());

    }

    @Override
    public UserInfoVO userLoginByPhoneNumber(String phoneNumber) {
        UserInfo userInfo = userInfoDao.selectUserInfoByLoginName(phoneNumber);

        if (userInfo == null) {
            throw BusinessException.newInstance(AppHttpCodeEnum.LOGIN_ERROR);
        }
        Integer userId = userInfo.getUserId();

        // 根据userId获取学生信息
        LambdaQueryWrapper<StudentInfo> studentQueryWrapper = new LambdaQueryWrapper<>();
        studentQueryWrapper.eq(StudentInfo::getUserId, userId);
        StudentInfo studentInfo = studentInfoDao.selectOne(studentQueryWrapper);

        // 如果不是学生，就获取教师信息
        TeacherInfo teacherInfo = null;
        if (studentInfo == null) {
            LambdaQueryWrapper<TeacherInfo> teacherQueryWrapper = new LambdaQueryWrapper<>();
            teacherQueryWrapper.eq(TeacherInfo::getUserId, userId);
            teacherInfo = teacherInfoDao.selectOne(teacherQueryWrapper);
        }
        UserInfoVO userInfoVO = new UserInfoVO();
        userInfoVO.setUserInfo(userInfo);
        userInfoVO.setStudentInfo(studentInfo);
        userInfoVO.setTeacherInfo(teacherInfo);
        return userInfoVO;
    }


    @Override
    @Transactional
    public Boolean registerTeacher(String loginName, String password, String userName, String teacherNumber, String qualificationImg, Integer schoolId, String schoolName) {
        /*1.添加新用户*/
        UserInfo newUser = new UserInfo();
        newUser.setLoginName(loginName);
        newUser.setPassword(password);
        newUser.setUserName(userName);
        userInfoDao.insert(newUser);
        /*2.获取该新用户的userId*/
        Integer userId = userInfoDao.getUserInfoByLoginName(newUser.getLoginName()).getUserId();
        /*3.将该用户添加为教师*/
        TeacherInfo newTeacher = new TeacherInfo();
        newTeacher.setUserId(userId);
        newTeacher.setTeacherNumber(teacherNumber);
        newTeacher.setQualificationImg(qualificationImg);
        newTeacher.setSchoolId(schoolId);
        newTeacher.setUserName(userName);
        newTeacher.setSchoolName(schoolName);
        if (userInfoDao.insertTeacher(newTeacher) != 0) {
            return true;
        }
        return false;
    }


    @Override
    @Transactional
    public Boolean registerStudent(String loginName, String password, String userName,
                                   String studentNumber, Integer schoolId, String schoolName) {
        /*1.添加新用户*/
        UserInfo newUser = new UserInfo();
        newUser.setLoginName(loginName);
        newUser.setPassword(password);
        newUser.setUserName(userName);
        userInfoDao.insert(newUser);
        /*2.获取该新用户的userId*/
        Integer userId = userInfoDao.getUserInfoByLoginName(newUser.getLoginName()).getUserId();
        /*3.将该用户添加为学生*/
        StudentInfo newStudent = new StudentInfo();
        newStudent.setUserId(userId);
        newStudent.setUserName(userName);
        newStudent.setStudentNumber(studentNumber);
        newStudent.setSchoolId(schoolId);
        newStudent.setSchoolName(schoolName);
        if (userInfoDao.insertStudent(newStudent) != 0) {
            return true;
        } else return false;
    }

    @Override
    public Boolean updatePassword(String loginName, String newPassword) {
        UserInfo userInfo = new UserInfo();
        userInfo.setLoginName(loginName);
        userInfo.setPassword(newPassword);
        if (userInfoDao.updatePassword(userInfo) != 0) {
            return true;
        }
        return true;
    }

    @Override
    public GetUserInfoVO getUserInfo(SaSession session) {
        //获取session中的用户信息
        UserInfoVO userInfoVO = (UserInfoVO) session.get(SystemConstants.SESSION_USER_KEY);
        // 获取用户信息
        UserInfo userInfo = userInfoVO.getUserInfo();
        // 获取用户信息中的角色列表
        List<RoleInfo> roleList = userInfo.getRoleList();
        // 获取用户信息中的角色列表中的角色名，作为List<String>返回
        List<String> roleArray = roleList.stream().map(RoleInfo::getRoleName).collect(Collectors.toList());
        GetUserInfoVO getUserInfoVO = BeanCopyUtils.copyBean(userInfo, GetUserInfoVO.class);
        // 设置角色列表
        getUserInfoVO.setRoleList(roleArray);
        return getUserInfoVO;
    }
}

