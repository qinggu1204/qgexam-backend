package com.qgexam.user.controller;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import com.qgexam.common.core.api.AppHttpCodeEnum;
import com.qgexam.common.core.api.ResponseResult;
import com.qgexam.common.core.constants.SystemConstants;
import com.qgexam.common.web.base.BaseController;
import com.qgexam.user.pojo.DTO.*;
import com.qgexam.user.pojo.VO.UserInfoVO;
import com.qgexam.user.service.MessageCodeService;
import com.qgexam.user.service.SchoolInfoService;
import com.qgexam.user.service.UserInfoService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Validated
@RestController
@RequestMapping("/user")
public class UserInfoController extends BaseController {

    @DubboReference
    private UserInfoService userInfoService;
    @DubboReference
    private MessageCodeService messageCodeService;

    @DubboReference
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
        return ResponseResult.okResult(schoolInfoService.getSchoolInfoList());
    }

    /**
     * @description 从session中获取用户信息
     * @return com.qgexam.common.core.api.ResponseResult
     * @author peter guo
     * @date 2022/12/14 19:10:29
     */
    @GetMapping("/common/getUserInfo")
    public ResponseResult getUserInfo() {
        // 获取session
        SaSession session = StpUtil.getSession();
        return ResponseResult.okResult(userInfoService.getUserInfo(session));
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
     * 教师注册
     *
     * @param teacherRegisterDTO 教师填写于表单中的信息
     * @return 注册结果
     */
    @PostMapping("/tea/register")
    public ResponseResult teacherRegister(@RequestBody @Validated TeacherRegisterDTO teacherRegisterDTO){
        if(userInfoService.registerTeacher(teacherRegisterDTO.getPhoneNumber(),teacherRegisterDTO.getPassword(),teacherRegisterDTO.getTeacherName(),
                teacherRegisterDTO.getTeacherNumber(),teacherRegisterDTO.getQualificationImg(),teacherRegisterDTO.getSchoolId(),teacherRegisterDTO.getSchoolName())){
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