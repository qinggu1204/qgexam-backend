package com.qgexam.user.controller;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import com.qgexam.common.core.api.AppHttpCodeEnum;
import com.qgexam.common.core.api.ResponseResult;
import com.qgexam.common.web.base.BaseController;
import com.qgexam.user.pojo.DTO.JoinCourseDTO;
import com.qgexam.user.pojo.DTO.UpdateStudentInfoDTO;
import com.qgexam.user.pojo.VO.GetStudentInfoVO;
import com.qgexam.user.service.StudentInfoService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/stu")
public class StudentInfoController extends BaseController {

    @DubboReference
    private StudentInfoService studentInfoService;

    /**
     * @return com.qgexam.common.core.api.ResponseResult
     * @auther peter guo
     * @date 2022/12/15 14:37:25
     */
    @GetMapping("/getStudentInfo")
    public ResponseResult getStudentInfo() {
        SaSession session = StpUtil.getSession();
        return ResponseResult.okResult(studentInfoService.getStudentInfo(session));
    }

    /**
     * @return com.qgexam.common.core.api.ResponseResult
     * @auther ljy
     * @date 2022/12/16 16:31:42
     */
    @PutMapping("/updateStudentInfo")
    public ResponseResult updateStudentInfo(@RequestBody @Validated UpdateStudentInfoDTO updateStudentInfoDTO) {
        if(studentInfoService.updateStudentInfo(getUserId(), updateStudentInfoDTO.getLoginName(),updateStudentInfoDTO.getHeadImg(),updateStudentInfoDTO.getFaceImg()))
            return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
    }

    /**
     * @return com.qgexam.common.core.api.ResponseResult
     * @auther ljy
     * @date 2022/12/16 20:31:42
     */
    @PostMapping("/joinCourse")
    public ResponseResult joinCourse(@RequestBody @Validated JoinCourseDTO joinCourseDTO) {
        SaSession session = StpUtil.getSession();
        GetStudentInfoVO getStudentInfoVO = studentInfoService.getStudentInfo(session);
        if(studentInfoService.joinCourse(getStudentId(), getStudentInfoVO.getUserName(),getStudentInfoVO.getStudentNumber(),joinCourseDTO.getCourseId()))
            return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
    }
}
