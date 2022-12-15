package com.qgexam.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qgexam.user.pojo.PO.StudentInfo;

/**
 * 学生表(StudentInfo)表服务接口
 *
 * @author peter guo
 * @since 2022-12-15 14:28:41
 */
public interface StudentInfoService extends IService<StudentInfo> {

    StudentInfo getStudentInfoByUserId(Integer userId);
}

