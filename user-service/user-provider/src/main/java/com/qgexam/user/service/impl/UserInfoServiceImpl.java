package com.qgexam.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qgexam.user.dao.UserInfoDao;
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
}

