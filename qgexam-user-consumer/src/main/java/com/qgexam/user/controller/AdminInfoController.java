package com.qgexam.user.controller;

import com.qgexam.common.core.api.AppHttpCodeEnum;
import com.qgexam.common.core.api.ResponseResult;
import com.qgexam.common.web.base.BaseController;
import com.qgexam.user.pojo.DTO.AddNeteacherDTO;
import com.qgexam.user.pojo.DTO.GetStudentDTO;
import com.qgexam.user.pojo.DTO.UpdateStudentNumberDTO;
import com.qgexam.user.pojo.DTO.UpdateTeacherNumberDTO;
import com.qgexam.user.service.AdminInfoService;
import com.qgexam.user.service.NeTeacherInfoService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/admin")
public class AdminInfoController extends BaseController {
    @DubboReference
    private AdminInfoService adminInfoService;
    @DubboReference
    private NeTeacherInfoService neTeacherInfoService;

    /**
     * @description 设置教务老师
     * @return com.qgexam.common.core.api.ResponseResult
     * @author ljy
     * @date 2022/1/6 21:21:43
     */
    @PostMapping("/addNeteacher")
    public ResponseResult getMessageList(@RequestBody @Validated AddNeteacherDTO addNeteacherDTO) {
        if (adminInfoService.addNeteacher(addNeteacherDTO.getUserId())) {
            return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
    }

    /**
     * @description 管理员查看任课教师列表
     * @return com.qgexam.common.core.api.ResponseResult
     * @author ljy
     * @date 2022/1/6 21:21:43
     */
    @GetMapping("/getTeacherList")
    public ResponseResult getTeacherList(Integer currentPage, Integer pageSize, Integer schoolId, String loginName) {
        return ResponseResult.okResult(adminInfoService.getTeacherList(currentPage, pageSize, schoolId,2, loginName));
    }

    /**
     * @description 管理员查看教务教师列表
     * @return com.qgexam.common.core.api.ResponseResult
     * @author ljy
     * @date 2022/1/6 21:21:43
     */
    @GetMapping("/getNeteacherList")
    public ResponseResult getNeteacherList(Integer currentPage, Integer pageSize, Integer schoolId, String loginName) {
        return ResponseResult.okResult(adminInfoService.getTeacherList(currentPage, pageSize, schoolId,3, loginName));
    }
    @GetMapping("/getStudentList")
    public ResponseResult getStudentList(@Validated GetStudentDTO getStudentDTO, Integer currentPage, Integer pageSize){
        return ResponseResult.okResult(adminInfoService.getStudentList(getStudentDTO.getSchoolId(),getStudentDTO.getLoginName(),currentPage,pageSize));
    }

    @PutMapping("/updateStudentNumber")
    public ResponseResult updateStudentNumber(@RequestBody @Validated UpdateStudentNumberDTO updateStudentNumberDTO){
        return  ResponseResult.okResult(adminInfoService.updateStudentNumber(updateStudentNumberDTO.getStudentId(),updateStudentNumberDTO.getNewStudentNumber()));
    }

    @PutMapping("/updateTeacherNumber")
    public ResponseResult updateTeacherNumber(@RequestBody @Validated UpdateTeacherNumberDTO updateTeacherNumberDTO){
        return  ResponseResult.okResult(adminInfoService.updateTeacherNumber(updateTeacherNumberDTO.getTeacherId(),updateTeacherNumberDTO.getNewTeacherNumber()));
    }
    @GetMapping("/getChapterBySubject/{subjectId}")
    public ResponseResult getChapterBySubject(@PathVariable Integer subjectId){
        return ResponseResult.okResult(neTeacherInfoService.getChapterInfoList(subjectId));
    }
}
