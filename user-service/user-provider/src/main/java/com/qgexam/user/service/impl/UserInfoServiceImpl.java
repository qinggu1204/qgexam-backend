package com.qgexam.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qgexam.user.dao.SchoolInfoDao;
import com.qgexam.user.dao.UserInfoDao;
import com.qgexam.user.pojo.DTO.StudentRegisterDTO;
import com.qgexam.user.pojo.DTO.TeacherRegisterDTO;
import com.qgexam.user.pojo.PO.StudentInfo;
import com.qgexam.user.pojo.PO.TeacherInfo;
import com.qgexam.user.pojo.PO.UserInfo;
import com.qgexam.user.service.UserInfoService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

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
    private SchoolInfoDao schoolInfoDao;

    @Override
    public UserInfo getUserInfoByLoginName(String loginName) {
        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserInfo::getLoginName, loginName);
        UserInfo userInfo = userInfoDao.selectOne(queryWrapper);
        return userInfo;
    }

    @Override
    public List<String> getRoleListByUserId(Integer id) {
        List<String> roleNameList = userInfoDao.selectRoleListById(id);
        return roleNameList;
    }

    @Override
    public UserInfo getUserInfoByPhoneNumber(String phoneNumber) {
        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserInfo::getLoginName, phoneNumber);
        UserInfo userInfo = userInfoDao.selectOne(queryWrapper);
        return userInfo;
    }

    public UserInfo getUserInfoById(Integer id) {
        return userInfoDao.getUserInfoById(id);
    }

    @Override
    public void registerTeacher(TeacherRegisterDTO teacherRegisterDTO){
        /*1.添加新用户*/
        UserInfo newUser=new UserInfo();
        newUser.setLoginName(teacherRegisterDTO.getPhoneNumber());
        newUser.setPassword(teacherRegisterDTO.getPassword());
        newUser.setUserName(teacherRegisterDTO.getUserName());
        userInfoDao.insert(newUser);
        /*2.获取该新用户的userId*/
        Integer userId = userInfoDao.getUserInfoByLoginName(newUser.getLoginName()).getUserId();
        /*3.获取该用户的学校名*/
        String schoolName = schoolInfoDao.queryById(teacherRegisterDTO.getSchoolId()).getSchoolName();
        /*4.将该用户添加为教师*/
        TeacherInfo newTeacher=new TeacherInfo();
        newTeacher.setUserId(userId);
        newTeacher.setTeacherNumber(teacherRegisterDTO.getTeacherNumber());
        newTeacher.setQualificationImg(teacherRegisterDTO.getQualificationImg());
        newTeacher.setSchoolId(teacherRegisterDTO.getSchoolId());
        newTeacher.setUserName(teacherRegisterDTO.getUserName());
        newTeacher.setSchoolName(schoolName);
        userInfoDao.insertTeacher(newTeacher);
    }

    @Override
    public void registerStudent(StudentRegisterDTO studentRegisterDTO){
        /*1.添加新用户*/
        UserInfo newUser=new UserInfo();
        newUser.setLoginName(studentRegisterDTO.getPhoneNumber());
        newUser.setPassword(studentRegisterDTO.getPassword());
        newUser.setUserName(studentRegisterDTO.getUserName());
        userInfoDao.insert(newUser);
        /*2.获取该新用户的userId*/
        Integer userId = userInfoDao.getUserInfoByLoginName(newUser.getLoginName()).getUserId();
        /*3.获取该用户的学校名*/
        String schoolName = schoolInfoDao.queryById(studentRegisterDTO.getSchoolId()).getSchoolName();
        /*4.将该用户添加为学生*/
        StudentInfo newStudent=new StudentInfo();
        newStudent.setUserId(userId);
        newStudent.setUserName(studentRegisterDTO.getUserName());
        newStudent.setStudentNumber(studentRegisterDTO.getStudentNumber());
        newStudent.setSchoolId(studentRegisterDTO.getSchoolId());
        newStudent.setSchoolName(schoolName);
        userInfoDao.insertStudent(newStudent);
    }

    @Override
    public void updatePassword(String loginName, String newPassword) {
        userInfoDao.updatePassword(loginName, newPassword);
    }
}

