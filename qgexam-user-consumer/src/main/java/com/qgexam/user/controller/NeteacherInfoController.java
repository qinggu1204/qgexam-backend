package com.qgexam.user.controller;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import com.qgexam.common.core.api.ResponseResult;
import com.qgexam.user.pojo.DTO.ArrangeInvigilationDTO;
import com.qgexam.user.service.NeTeacherInfoService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/netea")
public class NeteacherInfoController {

    @DubboReference
    private NeTeacherInfoService neTeacherInfoService;

    /**
     * @description 获取教师信息
     * @return com.qgexam.common.core.api.ResponseResult
     * @aythor peter guo
     * @date 2022/12/21 19:32:44
     */
    @PostMapping("/createPaper")
    public ResponseResult getStudentList(){
        return ResponseResult.okResult();
    }

    /**
     * 获取排考结果
     */
    @GetMapping("/getInvigilationInfo/{examinationId}")
    public ResponseResult getInvigilationInfo(@PathVariable("examinationId") Integer examinationId){

        return ResponseResult.okResult();
    }

    /**
     * 获取试卷列表
     */
    @GetMapping("/getPaperList")
    public ResponseResult getPaperList(Integer currentPage, Integer pageSize){
        SaSession session= StpUtil.getSession();
        return  ResponseResult.okResult(neTeacherInfoService.getExaminationPaperList(session,currentPage,pageSize));
    }

    /**
     * 安排监考任务
     */
    @PostMapping("/arrangeInvigilation")
    public  ResponseResult arrangeInvigilation(@RequestBody @Validated ArrangeInvigilationDTO arrangeInvigilationDTO){
        return ResponseResult.okResult(neTeacherInfoService.arrangeInvigilation(arrangeInvigilationDTO.getExaminationId()));
    }

}
