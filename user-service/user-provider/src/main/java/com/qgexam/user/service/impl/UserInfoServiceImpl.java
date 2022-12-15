package com.qgexam.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qgexam.user.dao.SchoolInfoDao;
import com.qgexam.user.dao.UserInfoDao;
import com.qgexam.user.pojo.PO.StudentInfo;
import com.qgexam.user.pojo.PO.TeacherInfo;
import com.qgexam.user.pojo.PO.UserInfo;
import com.qgexam.user.service.UserInfoService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public Boolean registerTeacher(String loginName,String password,String userName,String teacherNumber, String qualificationImg,Integer schoolId,String schoolName){
        /*1.添加新用户*/
        UserInfo newUser=new UserInfo();
        newUser.setLoginName(loginName);
        newUser.setPassword(password);
        newUser.setUserName(userName);
        userInfoDao.insert(newUser);
        /*2.获取该新用户的userId*/
        Integer userId = userInfoDao.getUserInfoByLoginName(newUser.getLoginName()).getUserId();
        /*3.将该用户添加为教师*/
        TeacherInfo newTeacher=new TeacherInfo();
        newTeacher.setUserId(userId);
        newTeacher.setTeacherNumber(teacherNumber);
        newTeacher.setQualificationImg(qualificationImg);
        newTeacher.setSchoolId(schoolId);
        newTeacher.setUserName(userName);
        newTeacher.setSchoolName(schoolName);
        if(userInfoDao.insertTeacher(newTeacher)!=0){
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public Boolean registerStudent(String loginName,String password,String userName,
                                String studentNumber,Integer schoolId,String schoolName){
        /*1.添加新用户*/
        UserInfo newUser=new UserInfo();
        newUser.setLoginName(loginName);
        newUser.setPassword(password);
        newUser.setUserName(userName);
        userInfoDao.insert(newUser);
        /*2.获取该新用户的userId*/
        Integer userId = userInfoDao.getUserInfoByLoginName(newUser.getLoginName()).getUserId();
        /*3.将该用户添加为学生*/
        StudentInfo newStudent=new StudentInfo();
        newStudent.setUserId(userId);
        newStudent.setUserName(userName);
        newStudent.setStudentNumber(studentNumber);
        newStudent.setSchoolId(schoolId);
        newStudent.setSchoolName(schoolName);
        if(userInfoDao.insertStudent(newStudent)!=0){
            return true;
        }
        else return false;
    }

    @Override
    public Boolean updatePassword(String loginName, String newPassword) {
        UserInfo userInfo=new UserInfo();
        userInfo.setLoginName(loginName);
        userInfo.setPassword(newPassword);
        if(userInfoDao.updatePassword(userInfo)!=0){
            return true;
        }
        return true;
    }
}

