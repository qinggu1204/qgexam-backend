package com.qgexam.user.controller;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import com.qgexam.common.core.api.AppHttpCodeEnum;
import com.qgexam.common.core.api.ResponseResult;
import com.qgexam.common.web.base.BaseController;
import com.qgexam.user.pojo.DTO.ArrangeInvigilationDTO;
import com.qgexam.user.pojo.DTO.CreateExamDTO;
import com.qgexam.user.pojo.DTO.GetInvigilationInfoDTO;
import com.qgexam.user.service.NeTeacherInfoService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/netea")
public class NeteacherInfoController extends BaseController {

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

    /**
     * @description 根据学科查询学科章节
     * @return com.qgexam.common.core.api.ResponseResult
     * @aythor ljy
     * @date 2022/12/25 20:10:44
     */
    @GetMapping("/getChapterBySubject/{subjectId}")
    public ResponseResult getChapterBySubject(@PathVariable Integer subjectId){
        return ResponseResult.okResult(neTeacherInfoService.getChapterInfoList(subjectId));
    }

    /**
     * @description 创建（发布）考试
     * @return com.qgexam.common.core.api.ResponseResult
     * @aythor ljy
     * @date 2022/12/28 15:14:42
     */
    @PostMapping("/createExam")
    public ResponseResult createExam(@RequestBody @Validated CreateExamDTO createExamDTO) {
        if (neTeacherInfoService.createExamination(getUserId(),createExamDTO)) {
            return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
    }

    /**
     * @description 教务教师获取排考结果
     * @param
     * @return
     * @author yzw
     * @date 2023/1/6 10:43:18
     */
    @GetMapping("/getInvigilationInfo/{examinationId}")
    public ResponseResult getInvigilationInfo(@PathVariable("examinationId") Integer examinationId,
                                              Integer currentPage, Integer pageSize){
        return ResponseResult.okResult(neTeacherInfoService.getInvigilationInfo(new GetInvigilationInfoDTO(examinationId,currentPage,pageSize)));
    }

    @GetMapping("/previewPaper/{examinationPaperId}")
    public ResponseResult previewPaper(@PathVariable("examinationPaperId") Integer examinationPaperId){
        return ResponseResult.okResult(neTeacherInfoService.previewPaper(examinationPaperId));
    }
}
