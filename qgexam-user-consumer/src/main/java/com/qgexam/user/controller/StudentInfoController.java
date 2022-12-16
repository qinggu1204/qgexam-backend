package com.qgexam.user.controller;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import com.qgexam.common.core.api.AppHttpCodeEnum;
import com.qgexam.common.core.api.ResponseResult;
import com.qgexam.common.core.constants.SystemConstants;
import com.qgexam.common.core.utils.BeanCopyUtils;
import com.qgexam.user.pojo.DTO.GetMessageCodeDTO;
import com.qgexam.user.pojo.DTO.UpdateStudentInfoDTO;
import com.qgexam.user.pojo.PO.StudentInfo;
import com.qgexam.user.pojo.PO.UserInfo;
import com.qgexam.user.pojo.VO.GetStudentInfoVO;
import com.qgexam.user.pojo.VO.UserInfoVO;
import com.qgexam.user.service.StudentInfoService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stu")
public class StudentInfoController {

    @Reference
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
     * @date 2022/12/15 14:37:25
     */
    @GetMapping("/updateStudentInfo")
    public ResponseResult updateStudentInfo(@Validated UpdateStudentInfoDTO updateStudentInfoDTO) {
        if(studentInfoService.updateStudentInfo(updateStudentInfoDTO.getLoginName(),updateStudentInfoDTO.getHeadImg(),updateStudentInfoDTO.getFaceImg()))
            return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
    }
}
