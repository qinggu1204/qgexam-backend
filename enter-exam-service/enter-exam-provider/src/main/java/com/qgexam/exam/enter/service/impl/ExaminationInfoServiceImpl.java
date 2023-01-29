package com.qgexam.exam.enter.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qgexam.common.core.utils.BeanCopyUtils;

import com.qgexam.exam.enter.dao.CourseInfoDao;
import com.qgexam.exam.enter.dao.ExaminationInfoDao;
import com.qgexam.exam.enter.pojo.DTO.GetExamListDTO;
import com.qgexam.exam.enter.pojo.PO.CourseInfo;
import com.qgexam.exam.enter.pojo.PO.ExaminationInfo;
import com.qgexam.exam.enter.pojo.VO.GetExamListVO;
import com.qgexam.exam.enter.service.ExaminationInfoService;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 考试信息表(ExaminationInfo)表服务实现类
 *
 * @author lamb007
 * @since 2023-01-06 14:19:39
 */
@DubboService
public class ExaminationInfoServiceImpl extends ServiceImpl<ExaminationInfoDao, ExaminationInfo> implements ExaminationInfoService {


    @Autowired
    private ExaminationInfoDao examinationInfoDao;

    @Autowired
    private CourseInfoDao courseInfoDao;

    @Override
    public IPage<GetExamListVO> getExamList(GetExamListDTO getExamListDTO) {
        Integer courseId = getExamListDTO.getCourseId();
        // courseId为空，查询所有课程的考试信息
        // 查询改学生的所有课程
        if (courseId == null) {
            IPage<ExaminationInfo> page = new Page<>(getExamListDTO.getCurrentPage(), getExamListDTO.getPageSize());
            List<CourseInfo> courseInfoList = courseInfoDao.selectCourseInfoListByStudentId(getExamListDTO.getStudentId());
            List<Integer> courseIdList = courseInfoList.stream().map(CourseInfo::getCourseId).collect(Collectors.toList());
            if (courseIdList.isEmpty()) {
                return new Page<>();
            }
            IPage<GetExamListVO> pageVO = examinationInfoDao.selectAllExaminationInfo(page, courseIdList);
            return pageVO;
        }
        // courseId不为空，查询该课程的考试信息
        IPage<ExaminationInfo> page = new Page<>(getExamListDTO.getCurrentPage(), getExamListDTO.getPageSize());
        List<Integer> courseIdList = new ArrayList<>();
        courseIdList.add(courseId);
        IPage<GetExamListVO> pageVO = examinationInfoDao.selectAllExaminationInfo(page, courseIdList);

        return pageVO;
    }


}

