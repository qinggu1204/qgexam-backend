package com.qgexam.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qgexam.user.dao.UserInfoDao;
import com.qgexam.user.pojo.PO.UserInfo;
import com.qgexam.user.service.UserInfoService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 用户信息表(UserInfo)表服务实现类
 *
 * @author yzw
 * @since 2022-12-10 11:29:21
 */
@DubboService
public class UserInfoServiceImpl extends ServiceImpl<UserInfoDao, UserInfo> implements UserInfoService {

    @Autowired
    private UserInfoDao userInfoDao;

    @Override
    public UserInfo getUserInfoByLoginName(String loginName) {
        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserInfo::getLoginName, loginName);
        UserInfo userInfo = userInfoDao.selectOne(queryWrapper);
        return userInfo;
    }
}

