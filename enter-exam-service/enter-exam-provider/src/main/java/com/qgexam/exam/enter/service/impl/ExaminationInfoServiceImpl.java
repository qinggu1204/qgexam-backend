package com.qgexam.exam.enter.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qgexam.common.core.utils.BeanCopyUtils;

import com.qgexam.exam.enter.dao.ExaminationInfoDao;
import com.qgexam.exam.enter.pojo.DTO.GetExamListDTO;
import com.qgexam.exam.enter.pojo.VO.GetExamListVO;
import com.qgexam.exam.enter.service.ExaminationInfoService;
import com.qgexam.user.pojo.PO.ExaminationInfo;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

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

    @Override
    public IPage<GetExamListVO> getExamList(GetExamListDTO getExamListDTO) {
        IPage<ExaminationInfo> page = new Page<>(getExamListDTO.getCurrentPage(), getExamListDTO.getPageSize());

        examinationInfoDao.selectAllExaminationInfo(page, getExamListDTO);
        List<ExaminationInfo> records = page.getRecords();
        List<GetExamListVO> getExamListVOS = BeanCopyUtils.copyBeanList(records, GetExamListVO.class);

        IPage<GetExamListVO> pageVO = BeanCopyUtils.copyBean(page, Page.class);
        pageVO.setRecords(getExamListVOS);
        return pageVO;
    }





}

