package com.qgexam.user.controller;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import com.qgexam.common.core.api.AppHttpCodeEnum;
import com.qgexam.common.core.api.ResponseResult;
import com.qgexam.common.core.constants.SystemConstants;
import com.qgexam.common.core.utils.BeanCopyUtils;
import com.qgexam.common.redis.utils.RedisCache;
import com.qgexam.user.pojo.DTO.StudentRegisterDTO;
import com.qgexam.user.pojo.DTO.UpdatePasswordDTO;
import com.qgexam.user.pojo.DTO.UserLoginByPhoneNumberDTO;
import com.qgexam.user.pojo.DTO.UserLoginByUsernameDTO;
import com.qgexam.user.pojo.PO.SchoolInfo;
import com.qgexam.user.pojo.PO.UserInfo;
import com.qgexam.user.pojo.VO.GetSchoolInfoVO;
import com.qgexam.user.pojo.VO.GetUserInfoByIdVO;
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


    @Autowired
    private RedisCache redisCache;


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
        String loginName = loginDTO.getLoginName();
        String password = loginDTO.getPassword();

        // 根据登录名查询改用户
        UserInfo userInfo = userInfoService.getUserInfoByLoginName(loginName);
        // 该用户不存在
        if (userInfo == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_ERROR);
        }
        // 密码不正确
        if (!password.equals(userInfo.getPassword())) {
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
        String phoneNumber = loginDTO.getPhoneNumber();
        String code = loginDTO.getCode();
        String redisCode = redisCache.getCacheObject(SystemConstants.LOGIN_REDIS_PREFIX + phoneNumber);


        // 验证码不正确
        if (redisCode == null || !redisCode.equals(code)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.CODE_ERROR);
        }
        // 根据手机号查询用户
        UserInfo userInfo = userInfoService.getUserInfoByPhoneNumber(phoneNumber);

        // 该用户不存在
        if (userInfo == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PHONENUMBER_NOT_EXIST);
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
        Integer userId = StpUtil.getLoginIdAsInt();
        UserInfo userInfo = userInfoService.getUserInfoById(userId);
        GetUserInfoByIdVO userInfoByIdVO = BeanCopyUtils.copyBean(userInfo, GetUserInfoByIdVO.class);
        return ResponseResult.okResult(userInfoByIdVO);
    }
    /**
     * 学生注册
     *
     * @param studentRegisterDTO 学生填写于表单中的信息
     * @return 注册结果
     */
    @PostMapping("/stu/register")
    public ResponseResult studentRegister(@RequestBody @Validated StudentRegisterDTO studentRegisterDTO){
        if(userInfoService.registerStudent(studentRegisterDTO.getPhoneNumber(),studentRegisterDTO.getPassword(),
                studentRegisterDTO.getStudentName(),studentRegisterDTO.getStudentNumber(),
                studentRegisterDTO.getSchoolId(),studentRegisterDTO.getSchoolName())){
            return ResponseResult.okResult();
        }
        else  return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
    }

    /**
     * 修改密码
     *
     * @param updatePasswordDTO 用户填写的表单信息
     * @return 修改结果
     */
    @PutMapping("/updatePassword")
    public ResponseResult updatePassword(@RequestBody @Validated UpdatePasswordDTO updatePasswordDTO){
        if(!messageCodeService.validateCode(updatePasswordDTO.getLoginName(),updatePasswordDTO.getCode())){
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        if(userInfoService.updatePassword(updatePasswordDTO.getLoginName(),updatePasswordDTO.getPassword())){
            return ResponseResult.okResult();
        }
        else  return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
    }
}