package com.qgexam.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qgexam.user.pojo.DTO.UserLoginByPhoneNumberDTO;
import com.qgexam.user.pojo.DTO.UserLoginByUsernameDTO;
import com.qgexam.user.pojo.PO.UserInfo;
import com.qgexam.user.pojo.VO.UserInfoVO;

import java.util.List;

/**
 * 用户信息表(UserInfo)表服务接口
 *
 * @author lamb007
 * @since 2022-12-10 20:12:05
 */
public interface UserInfoService extends IService<UserInfo> {




    UserInfo getUserInfoByPhoneNumber(String phoneNumber);



    UserInfoVO userLogin(UserLoginByUsernameDTO loginDTO);

    List<String> getRoleListByUserId(Integer id);



    UserInfoVO userLoginByPhoneNumber(String phoneNumber);
}

