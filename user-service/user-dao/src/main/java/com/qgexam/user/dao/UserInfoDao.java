package com.qgexam.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qgexam.user.pojo.PO.RoleInfo;
import com.qgexam.user.pojo.PO.UserInfo;

import java.util.List;

/**
 * 用户信息表(UserInfo)表数据库访问层
 *
 * @author lamb007
 * @since 2022-12-10 20:12:42
 */
public interface UserInfoDao extends BaseMapper<UserInfo> {

    List<String> selectRoleListById(Integer id);

    UserInfo getUserInfoById(Integer id);
}

