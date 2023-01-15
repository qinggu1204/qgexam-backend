package com.qgexam.exam.enter.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qgexam.exam.enter.pojo.DTO.GetExamListDTO;
import com.qgexam.exam.enter.pojo.VO.GetExamListVO;
import com.qgexam.user.pojo.PO.ExaminationInfo;

/**
 * 考试信息表(ExaminationInfo)表服务接口
 *
 * @author lamb007
 * @since 2023-01-06 14:18:16
 */
public interface ExaminationInfoService extends IService<ExaminationInfo> {

    IPage<GetExamListVO> getExamList(GetExamListDTO getExamListDTO);
}

