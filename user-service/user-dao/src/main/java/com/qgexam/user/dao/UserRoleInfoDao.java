package com.qgexam.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qgexam.user.pojo.PO.UserRoleInfo;

/**
 * 用户角色关联表(UserRoleInfo)表数据库访问层
 *
 * @author tageshi
 * @since 2022-12-16 18:37:03
 */
public interface UserRoleInfoDao extends BaseMapper<UserRoleInfo> {
    int insert(UserRoleInfo userRoleInfo);
    String getRoleNameByUserId(Integer userId);
}

