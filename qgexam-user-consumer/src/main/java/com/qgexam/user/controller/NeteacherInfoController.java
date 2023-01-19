package com.qgexam.user.controller;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import com.qgexam.common.core.api.AppHttpCodeEnum;
import com.qgexam.common.core.api.ResponseResult;
import com.qgexam.common.web.base.BaseController;
import com.qgexam.user.pojo.DTO.*;
import com.qgexam.user.service.NeTeacherInfoService;
import com.qgexam.user.service.SubjectInfoService;
import com.qgexam.user.service.TeacherInfoService;
import com.qgexam.user.service.UserInfoService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Validated
@RestController
@RequestMapping("/netea")
public class NeteacherInfoController extends BaseController {

    @DubboReference
    private NeTeacherInfoService neTeacherInfoService;
    @DubboReference
    private TeacherInfoService teacherInfoService;
    @DubboReference
    private UserInfoService userInfoService;
    @DubboReference
    private SubjectInfoService subjectInfoService;

    /**
     * @description 教务教师组卷
     * @return com.qgexam.common.core.api.ResponseResult
     * @author peter guo
     * @date 2022/12/21 19:32:44
     */
    @PostMapping("/createPaper")
    public ResponseResult createPaper(@RequestBody @Validated CreatePaperDTO createPaperDTO){
        //获取用户编号
        Integer userId = getUserId();
        //将相关信息插入试卷表
        neTeacherInfoService.createPaper(userId,createPaperDTO);
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
     * @description 获取学科列表
     * @return com.qgexam.common.core.api.ResponseResult
     * @author peter guo
     * @date 2022/12/24 16:48:39
     */
    @GetMapping("/getSubjectList")
    public ResponseResult getSubjectList(){
        return ResponseResult.okResult(neTeacherInfoService.getSubjectList());
    }

    /**
     * @description 分配阅卷任务
     * @return com.qgexam.common.core.api.ResponseResult
     * @author peter guo
     * @date 2022/12/25 21:16:22
     */
    @PostMapping("/distributeJudgeTask")
    public ResponseResult distributeJudgeTask(@RequestBody @Validated DistributeJudgeTaskDTO distributeJudgeTaskDTO){
        Integer examinationId = distributeJudgeTaskDTO.getExaminationId();
        Date endTime = distributeJudgeTaskDTO.getEndTime();
        neTeacherInfoService.distributeJudgeTask(examinationId,endTime);
        return ResponseResult.okResult();
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
    @GetMapping("/getTeacherInfo")
    public ResponseResult getTeacherInfo(){
        SaSession session=StpUtil.getSession();
        return ResponseResult.okResult(teacherInfoService.getTeacherInfo(session));
    }
    @PutMapping("/updateTeacherInfo")
    public ResponseResult updateTeacherInfo(@RequestBody @Validated UpdateTeacherInfoDTO updateTeacherInfoDTO) {
        userInfoService.updateTeacherInfo(getUserId(), updateTeacherInfoDTO);
        return ResponseResult.okResult();
    }

    @GetMapping("/previewPaper/{examinationPaperId}")
    public ResponseResult previewPaper(@PathVariable("examinationPaperId") Integer examinationPaperId){
        return ResponseResult.okResult(neTeacherInfoService.previewPaper(examinationPaperId));
    }
}
