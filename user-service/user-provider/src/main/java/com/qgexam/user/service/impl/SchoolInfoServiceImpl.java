package com.qgexam.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qgexam.user.dao.SchoolInfoDao;
import com.qgexam.user.pojo.PO.SchoolInfo;
import com.qgexam.user.service.SchoolInfoService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 学校表(SchoolInfo)表服务实现类
 *
 * @author peter guo
 * @since 2022-12-14 11:18:02
 */
@Service
public class SchoolInfoServiceImpl extends ServiceImpl<SchoolInfoDao, SchoolInfo> implements SchoolInfoService {

    @Autowired
    private SchoolInfoDao schoolInfoDao;

    @Override
    public List<SchoolInfo> getSchoolList() {
        LambdaQueryWrapper<SchoolInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(SchoolInfo::getSchoolId, SchoolInfo::getSchoolName);
        return schoolInfoDao.selectList(queryWrapper);
    }
}

