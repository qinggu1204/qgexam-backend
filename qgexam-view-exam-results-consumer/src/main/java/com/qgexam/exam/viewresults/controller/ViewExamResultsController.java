package com.qgexam.exam.viewresults.controller;

import com.qgexam.common.core.api.ResponseResult;
import com.qgexam.common.web.base.BaseController;
import com.qgexam.exam.viewresults.pojo.DTO.ErrorDTO;
import com.qgexam.exam.viewresults.service.AnswerPaperInfoService;
import com.qgexam.exam.viewresults.service.CourseInfoService;
import com.qgexam.exam.viewresults.service.ErrorquestionInfoService;
import com.qgexam.rabbit.constants.ViewExamResultsRabbitConstant;
import com.qgexam.rabbit.pojo.PO.ErrorQuestionDTO;
import com.qgexam.rabbit.service.RabbitService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * @author ljy
 * @description
 * @date 2023/1/6 14:37:41
 */
@Validated
@RestController
@RequestMapping("/stu/viewExamResults")
public class ViewExamResultsController extends BaseController {

    @DubboReference
    private CourseInfoService courseInfoService;
    @DubboReference
    private AnswerPaperInfoService answerPaperInfoService;
    @DubboReference
    private ErrorquestionInfoService errorquestionInfoService;

    @DubboReference(registry = "rabbitmqRegistry")
    private RabbitService rabbitService;

    /**
     * 学生查看课程成绩
     *
     * @return ResponseResult
     * @author ljy
     * @date 2023/1/6 15:59
     */

    @GetMapping("/getCourseScore")
    public ResponseResult getCourseScore(Integer courseId) {
        return ResponseResult.okResult(courseInfoService.getFinalScore(courseId,getStudentId()));
    }

    /**
     * 学生查看考试成绩明细
     *
     * @return ResponseResult
     * @author ljy
     * @date 2023/1/7 15:59
     */

    @GetMapping("/getExamScoreDetail")
    public ResponseResult getExamScoreDetail(Integer examinationId) {
        return ResponseResult.okResult(answerPaperInfoService.getExamScoreDetail(examinationId,getStudentId()));
    }

    /**
     * 考试错题加入错题集
     *
     * @return ResponseResult
     * @author ljy
     * @date 2023/1/14 15:59
     */

    @PostMapping("/addErrorQuestion")
    public ResponseResult addErrorQuestion(@RequestBody @Validated ErrorDTO errorDTO) {
        // 封装消息
        ErrorQuestionDTO errorQuestionDTO = new ErrorQuestionDTO(errorDTO.getExaminationId(),errorDTO.getQuestionId(),getStudentId());
        // 消息队列发送一条消息
        rabbitService.sendMessage(ViewExamResultsRabbitConstant.EXAM_VIEWRESULTS_EXCHANGE_NAME,
                ViewExamResultsRabbitConstant.EXAM_VIEWRESULTS_ROUTING_KEY,
                errorQuestionDTO);
        return ResponseResult.okResult();
    }

    /**
     * 学生查看错题集
     *
     * @return ResponseResult
     * @author ljy
     * @date 2023/1/14 15:59
     */

    @GetMapping("/getErrorQuestionList")
    public ResponseResult getErrorQuestionList(Integer courseId) {
        return ResponseResult.okResult(errorquestionInfoService.getErrorQuestionList(courseId,getStudentId()));
    }
}
