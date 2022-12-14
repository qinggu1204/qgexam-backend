package com.qgexam.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qgexam.user.pojo.PO.RoleInfo;
import com.qgexam.user.pojo.PO.UserInfo;

import java.util.List;

/**
 * 用户信息表(UserInfo)表服务接口
 *
 * @author lamb007
 * @since 2022-12-10 20:12:05
 */
public interface UserInfoService extends IService<UserInfo> {
    UserInfo getUserInfoByLoginName(String loginName);

    List<String> getRoleListByUserId(Integer id);


    UserInfo getUserInfoByPhoneNumber(String phoneNumber);

    UserInfo getUserInfoById(Integer id);

}

