package com.qgexam.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qgexam.user.pojo.PO.UserInfo;

/**
 * 用户信息表(UserInfo)表服务接口
 *
 * @author yzw
 * @since 2022-12-10 11:25:56
 */
public interface UserInfoService extends IService<UserInfo> {
    UserInfo getUserInfoByLoginName(String loginName);
}

