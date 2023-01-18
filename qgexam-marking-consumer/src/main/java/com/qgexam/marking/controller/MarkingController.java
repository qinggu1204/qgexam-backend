package com.qgexam.marking.controller;

import com.qgexam.common.core.api.ResponseResult;
import com.qgexam.common.web.base.BaseController;
import com.qgexam.marking.pojo.DTO.MarkingDTO;
import com.qgexam.marking.service.MarkingService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author peter guo
 * @description 教师控制器
 * @date 2022/12/28 20:20:58
 */
@RestController
@Validated
@RequestMapping("/tea/marking")
public class MarkingController extends BaseController {
    @DubboReference
    private MarkingService markingService;

    /**
     * @return com.qgexam.common.core.api.ResponseResult
     * @description 获取阅卷任务列表
     */
    @GetMapping("/getTaskList")
    public ResponseResult getTaskList(Integer currentPage, Integer pageSize) {
        //获取教师编号
        Integer teacherId = getTeacherId();
        return ResponseResult.okResult(markingService.getTaskList(teacherId, currentPage, pageSize));
    }

    @GetMapping("/getAnswerPaperList/{examinationId}")
    public ResponseResult getAnswerPaperList(@PathVariable Integer examinationId, Integer currentPage, Integer pageSize) {
        //获取教师编号
        Integer teacherId = getTeacherId();
        return ResponseResult.okResult(markingService.getAnswerPaperList(teacherId, examinationId, currentPage, pageSize));
    }

    /**
     * @param answerPaperId
     * @return
     * @description 根据编号获取答卷
     */
    @GetMapping("/getAnswerPaper/{answerPaperId}")
    public ResponseResult getAnswerPaper(@PathVariable Integer answerPaperId) {
        Integer teacherId = getTeacherId();
        return ResponseResult.okResult(markingService.getAnswerPaper(teacherId, answerPaperId));
    }

    /**
     * @param answerPaperId
     * @param questionList
     * @return
     * @description 教师打分
     */
    @PutMapping("/marking/{answerPaperId}")
    public ResponseResult marking(@PathVariable Integer answerPaperId, @RequestBody @Validated List<MarkingDTO> questionList) {
        markingService.marking(getTeacherId(),answerPaperId, questionList);
        return ResponseResult.okResult();
    }
}
