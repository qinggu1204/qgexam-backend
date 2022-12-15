package com.qgexam.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qgexam.common.core.api.AppHttpCodeEnum;
import com.qgexam.common.core.constants.SystemConstants;
import com.qgexam.common.core.exception.BusinessException;
import com.qgexam.common.core.utils.BeanCopyUtils;
import com.qgexam.common.redis.utils.RedisCache;
import com.qgexam.user.dao.StudentInfoDao;
import com.qgexam.user.dao.TeacherInfoDao;
import com.qgexam.user.dao.UserInfoDao;
import com.qgexam.user.pojo.DTO.UserLoginByPhoneNumberDTO;
import com.qgexam.user.pojo.DTO.UserLoginByUsernameDTO;
import com.qgexam.user.pojo.PO.RoleInfo;
import com.qgexam.user.pojo.PO.StudentInfo;
import com.qgexam.user.pojo.PO.TeacherInfo;
import com.qgexam.user.pojo.PO.UserInfo;
import com.qgexam.user.pojo.VO.UserInfoVO;
import com.qgexam.user.service.UserInfoService;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户信息表(UserInfo)表服务实现类
 *
 * @author lamb007
 * @since 2022-12-10 20:13:04
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoDao, UserInfo> implements UserInfoService {

    @Autowired
    private UserInfoDao userInfoDao;

    @Autowired
    private TeacherInfoDao teacherInfoDao;

    @Autowired
    private StudentInfoDao studentInfoDao;

    @Autowired
    private RedisCache redisCache;


    @Override
    public UserInfo getUserInfoByPhoneNumber(String phoneNumber) {
        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserInfo::getLoginName, phoneNumber);
        UserInfo userInfo = userInfoDao.selectOne(queryWrapper);
        return userInfo;
    }


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




}

