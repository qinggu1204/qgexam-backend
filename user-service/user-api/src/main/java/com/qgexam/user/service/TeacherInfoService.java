package com.qgexam.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qgexam.user.pojo.PO.TeacherInfo;
import com.qgexam.user.pojo.VO.StudentVO;

/**
 * 教师表(TeacherInfo)表服务接口
 *
 * @author yzw
 * @since 2022-12-16 15:58:51
 */
public interface TeacherInfoService extends IService<TeacherInfo> {

    IPage<StudentVO> getStudentList(Integer courseId, Integer currentPage, Integer pageSize);
}

