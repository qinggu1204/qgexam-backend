package com.qgexam.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qgexam.user.pojo.PO.SchoolInfo;
import com.qgexam.user.pojo.VO.SchoolInfoVO;

import java.util.List;

/**
 * 学校表(SchoolInfo)表服务接口
 *
 * @author peter guo
 * @since 2022-12-14 11:15:14
 */
public interface SchoolInfoService extends IService<SchoolInfo> {
    List<SchoolInfoVO> getSchoolInfoList();
}

