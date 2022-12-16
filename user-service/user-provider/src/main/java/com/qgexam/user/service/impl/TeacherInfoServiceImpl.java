package com.qgexam.user.service.impl;

import cn.dev33.satoken.session.SaSession;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qgexam.common.core.constants.SystemConstants;
import com.qgexam.common.core.utils.BeanCopyUtils;
import com.qgexam.user.dao.TeacherInfoDao;
import com.qgexam.user.pojo.PO.TeacherInfo;
import com.qgexam.user.pojo.PO.UserInfo;
import com.qgexam.user.pojo.VO.GetTeacherInfoVO;
import com.qgexam.user.pojo.VO.UserInfoVO;
import com.qgexam.user.service.TeacherInfoService;
import org.apache.dubbo.config.annotation.Service;

/**
 * 教师表(TeacherInfo)表服务实现类
 *
 * @author tageshi
 * @since 2022-12-16 17:29:35
 */
@Service
public class TeacherInfoServiceImpl extends ServiceImpl<TeacherInfoDao, TeacherInfo> implements TeacherInfoService {

    @Override
    public GetTeacherInfoVO getTeacherInfo(SaSession session) {
        /*获取用户信息*/
        UserInfoVO userInfoVO = (UserInfoVO) session.get(SystemConstants.SESSION_USER_KEY);
        UserInfo userInfo = userInfoVO.getUserInfo();
        /*获取教师信息*/
        TeacherInfo teacherInfo=userInfoVO.getTeacherInfo();
        GetTeacherInfoVO getTeacherInfoVO= BeanCopyUtils.copyFromManyBean(GetTeacherInfoVO.class, userInfo, teacherInfo);
        return getTeacherInfoVO;
    }
}

