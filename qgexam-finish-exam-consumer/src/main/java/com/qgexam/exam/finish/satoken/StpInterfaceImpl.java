package com.qgexam.exam.finish.satoken;

import cn.dev33.satoken.stp.StpInterface;
import com.qgexam.user.service.UserInfoService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author tageshi
 * @date 2023/1/13 16:02
 */
@Component //保证此类被SpringBoot扫描，完成Sa-Token的自定义权限验证扩展
public class StpInterfaceImpl implements StpInterface {
    @DubboReference(registry = "userRegistry")
    private UserInfoService userInfoService;

    @Override
    public List<String> getPermissionList(Object o, String s) {
        return null;
    }

    /**
     * 返回一个号所拥有的角色标识集合 (权限与角色可分开校验)
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        String userId = (String) loginId;
        List<String> roleNameList = userInfoService.getRoleListByUserId(Integer.valueOf(userId));
        return roleNameList;
    }

}
