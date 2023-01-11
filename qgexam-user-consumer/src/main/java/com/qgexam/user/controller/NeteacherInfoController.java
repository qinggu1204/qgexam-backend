package com.qgexam.user.controller;

import com.qgexam.common.core.api.ResponseResult;
import com.qgexam.common.web.base.BaseController;
import com.qgexam.user.pojo.DTO.CreatePaperDTO;
import com.qgexam.user.pojo.DTO.DistributeJudgeTaskDTO;
import com.qgexam.user.service.NeTeacherInfoService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Validated
@RestController
@RequestMapping("/netea")
public class NeteacherInfoController extends BaseController {

    @DubboReference
    private NeTeacherInfoService neTeacherInfoService;

    /**
     * @description 教务教师组卷
     * @return com.qgexam.common.core.api.ResponseResult
     * @author peter guo
     * @date 2022/12/21 19:32:44
     */
    @PostMapping("/createPaper")
    public ResponseResult createPaper(@RequestBody @Validated CreatePaperDTO createPaperDTO){
        //获取教师编号
        Integer teacherId = getTeacherId();
        //将相关信息插入试卷表
        neTeacherInfoService.createPaper(teacherId,createPaperDTO);
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
}
