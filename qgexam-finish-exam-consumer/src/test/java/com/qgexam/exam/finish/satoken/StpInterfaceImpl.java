package com.qgexam.exam.finish.satoken;

import cn.dev33.satoken.stp.StpInterface;
import com.qgexam.user.service.UserInfoService;
import org.springframework.data.annotation.Reference;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 自定义权限验证接口扩展
 */
@Component    // 保证此类被SpringBoot扫描，完成Sa-Token的自定义权限验证扩展
public class StpInterfaceImpl implements StpInterface {

    @Reference
    private UserInfoService userInfoService;

    @Override
    public List<String> getPermissionList(Object o, String s) {
        return null;
    }

    /**
     * 返回一个号所拥有的角色标识集合 (权限与角色可分开校验)账
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        String userId = (String) loginId;
        List<String> roleNameList = userInfoService.getRoleListByUserId(Integer.valueOf(userId));
        return roleNameList;
    }

}
