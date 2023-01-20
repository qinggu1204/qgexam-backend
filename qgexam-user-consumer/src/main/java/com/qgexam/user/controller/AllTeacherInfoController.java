package com.qgexam.user.controller;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import com.qgexam.common.core.api.ResponseResult;
import com.qgexam.common.core.constants.SystemConstants;
import com.qgexam.common.web.base.BaseController;
import com.qgexam.user.pojo.DTO.GetExamListDTO;
import com.qgexam.user.pojo.PO.RoleInfo;
import com.qgexam.user.pojo.PO.UserInfo;
import com.qgexam.user.pojo.VO.UserInfoVO;
import com.qgexam.user.service.TeacherInfoService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/alltea")
public class AllTeacherInfoController extends BaseController {

    @DubboReference
    private TeacherInfoService teacherInfoService;


    @GetMapping("/getScoreList")
    public ResponseResult getScoreList(Integer courseId,Integer currentPage,Integer pageSize) {
        return ResponseResult.okResult(teacherInfoService.getScoreList(courseId,currentPage,pageSize));
    }

    @GetMapping("/getExamList")
    public ResponseResult getExamList(Integer courseId,Integer currentPage,Integer pageSize) {
        GetExamListDTO getExamListDTO = new GetExamListDTO();
        getExamListDTO.setCourseId(courseId);
        getExamListDTO.setCurrentPage(currentPage);
        getExamListDTO.setPageSize(pageSize);
        getExamListDTO.setTeacherId(getTeacherId());
        SaSession session = StpUtil.getSession();
        //获取session中的用户信息
        UserInfoVO userInfoVO = (UserInfoVO) session.get(SystemConstants.SESSION_USER_KEY);
        getExamListDTO.setUserInfoVO(userInfoVO);

        return ResponseResult.okResult(teacherInfoService.getExamList(getExamListDTO));
    }


}
