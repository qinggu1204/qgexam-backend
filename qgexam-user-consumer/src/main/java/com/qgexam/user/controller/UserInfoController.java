package com.qgexam.user.controller;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import com.qgexam.common.core.api.AppHttpCodeEnum;
import com.qgexam.common.core.api.ResponseResult;
import com.qgexam.common.core.constants.SystemConstants;
import com.qgexam.common.core.utils.BeanCopyUtils;
import com.qgexam.common.redis.utils.RedisCache;
import com.qgexam.user.pojo.DTO.UserLoginByPhoneNumberDTO;
import com.qgexam.user.pojo.DTO.UserLoginByUsernameDTO;
import com.qgexam.user.pojo.PO.SchoolInfo;
import com.qgexam.user.pojo.PO.UserInfo;
import com.qgexam.user.pojo.VO.GetSchoolInfoVO;
import com.qgexam.user.pojo.VO.UserInfoVO;
import com.qgexam.user.service.MessageCodeService;
import com.qgexam.user.service.SchoolInfoService;
import com.qgexam.user.service.UserInfoService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Validated
@RestController
@RequestMapping("/user")
public class UserInfoController {

    @Reference
    private UserInfoService userInfoService;

    @Reference
    private MessageCodeService messageCodeService;



    @Reference
    private SchoolInfoService schoolInfoService;

    /**
     * @param loginDTO
     * @return com.qgexam.common.core.api.ResponseResult
     * @description 接收用户名和密码，进行登录
     * @author yzw
     * @date 2022/12/14 15:27:04
     */
    @PostMapping("/login")
    public ResponseResult userLoginByUsername(@RequestBody @Validated UserLoginByUsernameDTO loginDTO) {
        UserInfoVO userInfoVO = userInfoService.userLogin(loginDTO);
        // sa-token登录
        StpUtil.login(userInfoVO.getUserInfo().getUserId());
        // 获取token
        String token = StpUtil.getTokenValue();
        // 获取session
        SaSession session = StpUtil.getSession();
        // session中放入用户信息
        session.set(SystemConstants.SESSION_USER_KEY, userInfoVO);

        return ResponseResult.okResult(token);
    }


    /**
     * @param
     * @return com.qgexam.common.core.api.ResponseResult
     * @description 退出登录
     * @author yzw
     * @date 2022/12/14 15:27:04
     */
    @DeleteMapping("/common/logout")
    public ResponseResult logout() {
        StpUtil.logout();
        return ResponseResult.okResult();
    }


    /**
     * @description 手机号验证码登录
     * @param loginDTO
     * @return com.qgexam.common.core.api.ResponseResult
     * @author yzw
     * @date 2022/12/14 16:10:29
     */
    @PostMapping("/loginByCode")
    public ResponseResult loginByCode(@Validated @RequestBody UserLoginByPhoneNumberDTO loginDTO) {

        boolean checkCodeResult = messageCodeService.validateCode(loginDTO.getPhoneNumber(), loginDTO.getCode());
        if (!checkCodeResult) {
            return ResponseResult.errorResult(AppHttpCodeEnum.CODE_ERROR);
        }

        UserInfoVO userInfoVO = userInfoService.userLoginByPhoneNumber(loginDTO.getPhoneNumber());
        // sa-token登录
        StpUtil.login(userInfoVO.getUserInfo().getUserId());
        // 获取token
        String token = StpUtil.getTokenValue();
        // 获取session
        SaSession session = StpUtil.getSession();
        // session中放入用户信息
        session.set(SystemConstants.SESSION_USER_KEY, userInfoVO);

        return ResponseResult.okResult(token);
    }
    
    /**
     * @description 注册时获取学校信息
     * @return com.qgexam.common.core.api.ResponseResult
     * @author peter guo
     * @date 2022/12/14 19:10:29
     */
    @GetMapping("/getSchoolList")
    public ResponseResult getSchoolList() {
        List<SchoolInfo> schoolInfoList = schoolInfoService.getSchoolInfoList();
        List<GetSchoolInfoVO> schoolInfoVOList = new ArrayList<GetSchoolInfoVO>();
        for (SchoolInfo schoolInfo : schoolInfoList) {
            schoolInfoVOList.add(BeanCopyUtils.copyBean(schoolInfo, GetSchoolInfoVO.class));
        }
        return ResponseResult.okResult(schoolInfoVOList);
    }

    /**
     * @description 根据用户id获取用户信息
     * @return com.qgexam.common.core.api.ResponseResult
     * @aythor peter guo
     * @date 2022/12/14 19:10:29
     */
    @GetMapping("/common/getUserInfo")
    public ResponseResult getUserInfo() {
        //获取用户id
        SaSession session = StpUtil.getSession();
        UserInfo o = (UserInfo) session.get(SystemConstants.SESSION_USER_KEY);
        System.out.println(o);
//        UserInfo userInfo = userInfoService.getUserInfoById(userId);
//        GetUserInfoByIdVO userInfoByIdVO = BeanCopyUtils.copyBean(userInfo, GetUserInfoByIdVO.class);
        return ResponseResult.okResult(o);
    }
}
