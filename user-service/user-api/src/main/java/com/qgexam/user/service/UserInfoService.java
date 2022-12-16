package com.qgexam.user.service;

import cn.dev33.satoken.session.SaSession;
import com.baomidou.mybatisplus.extension.service.IService;

import com.qgexam.user.pojo.DTO.UpdateTeacherInfoDTO;
import com.qgexam.user.pojo.DTO.UserLoginByPhoneNumberDTO;
import com.qgexam.user.pojo.DTO.UserLoginByUsernameDTO;

import com.qgexam.user.pojo.PO.UserInfo;
import com.qgexam.user.pojo.VO.GetUserInfoVO;
import com.qgexam.user.pojo.VO.UserInfoVO;

import java.util.List;

/**
 * 用户信息表(UserInfo)表服务接口
 *
 * @author lamb007
 * @since 2022-12-10 20:12:05
 */
public interface UserInfoService extends IService<UserInfo> {


    UserInfoVO userLogin(UserLoginByUsernameDTO loginDTO);

    List<String> getRoleListByUserId(Integer id);


    UserInfoVO userLoginByPhoneNumber(String phoneNumber);

    Boolean registerTeacher(String loginName, String password, String userName, String teacherNumber, String qualificationImg, Integer schoolId, String schoolName);

    Boolean registerStudent(String loginName, String password, String userName, String studentNumber, Integer schoolId, String schoolName);

    Boolean updatePassword(String loginName, String newPassword);


    void updateTeacherInfo(Integer userId, UpdateTeacherInfoDTO updateTeacherInfoDTO);

    GetUserInfoVO getUserInfo(SaSession session);


}

