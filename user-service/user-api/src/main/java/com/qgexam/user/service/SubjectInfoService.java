package com.qgexam.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qgexam.user.pojo.PO.SubjectInfo;
import com.qgexam.user.pojo.VO.GetSubjectVO;

import java.util.List;

/**
 * 学科信息表(SubjectInfo)表服务接口
 *
 * @author tageshi
 * @since 2023-01-15 00:19:18
 */
public interface SubjectInfoService{
    public List<GetSubjectVO> getSubjectList();
}

