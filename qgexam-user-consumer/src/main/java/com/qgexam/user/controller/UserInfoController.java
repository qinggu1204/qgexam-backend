package com.qgexam.user.controller;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import com.qgexam.common.core.api.AppHttpCodeEnum;
import com.qgexam.common.core.api.ResponseResult;
import com.qgexam.common.core.constants.SystemConstants;
import com.qgexam.common.web.base.BaseController;
import com.qgexam.user.pojo.DTO.UserLoginByUsernameDTO;
import com.qgexam.user.pojo.PO.SchoolInfo;
import com.qgexam.user.pojo.PO.UserInfo;
import com.qgexam.user.service.SchoolInfoService;
import com.qgexam.user.service.UserInfoService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/user")
public class UserInfoController extends BaseController {

    @Reference
    private UserInfoService userInfoService;

    @Reference
    private SchoolInfoService schoolInfoService;

    @PostMapping("/login")
    public ResponseResult userLoginByUsername(@RequestBody @Validated UserLoginByUsernameDTO loginDTO) {
        String loginName = loginDTO.getLoginName();
        String password = loginDTO.getPassword();

        UserInfo userInfo = userInfoService.getUserInfoByLoginName(loginName);
        if (userInfo == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_ERROR);
        }
        if(!password.equals(userInfo.getPassword())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_ERROR);
        }
        // sa-token登录
        StpUtil.login(userInfo.getUserId());
        // 获取token
        String token = StpUtil.getTokenValue();
        // 获取session
        SaSession session = StpUtil.getSession();
        // session中放入用户信息
        session.set(SystemConstants.SESSION_KEY, userInfo);

        return ResponseResult.okResult(token);
    }

    @GetMapping("/getSchoolList")
    public ResponseResult getSchoolList() {
        return ResponseResult.okResult(schoolInfoService.getSchoolList());
    }

    @GetMapping("/common/getUserInfo")
    public ResponseResult getUserInfo() {
        Integer userId = StpUtil.getLoginIdAsInt();
        return ResponseResult.okResult(userInfoService.getUserInfoById(userId));
    }


}
