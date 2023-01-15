package com.qgexam.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qgexam.user.dao.SchoolInfoDao;
import com.qgexam.user.dao.SubjectInfoDao;
import com.qgexam.user.pojo.PO.SubjectInfo;
import com.qgexam.user.pojo.VO.GetSubjectVO;
import com.qgexam.user.service.SubjectInfoService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 学科信息表(SubjectInfo)表服务实现类
 *
 * @author tageshi
 * @since 2023-01-15 00:27:15
 */
@DubboService
public class SubjectInfoServiceImpl implements SubjectInfoService {
    @Autowired
    private SubjectInfoDao subjectInfoDao;
    @Override
    public List<GetSubjectVO> getSubjectList() {
        return subjectInfoDao.getSubjectInfo();
    }
}

