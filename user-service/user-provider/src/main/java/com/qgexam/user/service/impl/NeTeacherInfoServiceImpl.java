package com.qgexam.user.service.impl;

import cn.dev33.satoken.session.SaSession;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qgexam.common.core.constants.SystemConstants;
import com.qgexam.common.core.utils.BeanCopyUtils;
import com.qgexam.user.dao.TeacherInfoDao;
import com.qgexam.user.pojo.VO.GetCourseListVO;
import com.qgexam.user.pojo.VO.GetExaminationPaperVO;
import com.qgexam.user.pojo.VO.UserInfoVO;
import com.qgexam.user.service.NeTeacherInfoService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yzw
 * @description
 * @date 2022/12/21 20:01:03
 */
@DubboService
public class NeTeacherInfoServiceImpl implements NeTeacherInfoService {
    @Autowired
    private TeacherInfoDao teacherInfoDao;

    @Override
    public IPage<GetExaminationPaperVO> getExaminationPaperList(SaSession session, Integer currentPage, Integer pageSize) {
        IPage<GetExaminationPaperVO> page=new Page<>(currentPage,pageSize);
        /*获取用户信息*/
        UserInfoVO userInfoVO = (UserInfoVO) session.get(SystemConstants.SESSION_USER_KEY);
        List<GetExaminationPaperVO> getExaminationPaperVOList=teacherInfoDao.getExaminationPaperList(userInfoVO.getUserInfo().getUserId());
        page.setRecords(getExaminationPaperVOList);
        return page.convert(examinationPaper -> BeanCopyUtils.copyBean(examinationPaper, GetExaminationPaperVO.class));
    }
}
