package com.qgexam.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qgexam.common.core.utils.BeanCopyUtils;
import com.qgexam.user.dao.SchoolInfoDao;
import com.qgexam.user.pojo.PO.SchoolInfo;
import com.qgexam.user.pojo.VO.SchoolInfoVO;
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

    /**
     * 获取学校列表
     * @author peter guo
     * @date 2022-12-14 19:18:02
     * @return java.util.List<com.qgexam.user.pojo.VO.SchoolInfoVO>
     */
    @Override
    public List<SchoolInfoVO> getSchoolInfoList() {
        List<SchoolInfo> schoolInfos = schoolInfoDao.selectList(null);
        /*List<SchoolInfoVO> schoolInfoVOList = new ArrayList<SchoolInfoVO>();
        for (SchoolInfo schoolInfo : schoolInfos) {
            schoolInfoVOList.add(BeanCopyUtils.copyBean(schoolInfo, SchoolInfoVO.class));
        }*/
        return BeanCopyUtils.copyBeanList(schoolInfos, SchoolInfoVO.class);
    }
}

