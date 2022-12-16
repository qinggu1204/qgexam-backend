package com.qgexam.user.service.impl;

import cn.dev33.satoken.session.SaSession;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qgexam.common.core.constants.SystemConstants;
import com.qgexam.common.core.utils.BeanCopyUtils;
import com.qgexam.user.dao.StudentInfoDao;
import com.qgexam.user.dao.UserInfoDao;
import com.qgexam.user.pojo.DTO.UpdateStudentInfoDTO;
import com.qgexam.user.pojo.PO.StudentInfo;
import com.qgexam.user.pojo.PO.UserInfo;
import com.qgexam.user.pojo.VO.GetStudentInfoVO;
import com.qgexam.user.pojo.VO.UserInfoVO;
import com.qgexam.user.service.StudentInfoService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 学生表(StudentInfo)表服务实现类
 *
 * @author peter guo
 * @since 2022-12-15 14:29:30
 */
@Service
public class StudentInfoServiceImpl extends ServiceImpl<StudentInfoDao, StudentInfo> implements StudentInfoService {

    @Autowired
    private StudentInfoDao studentInfoDao;

    @Autowired
    private UserInfoDao userInfoDao;

    @Override
    public GetStudentInfoVO getStudentInfo(SaSession session) {
        // 获取session中的用户信息
        UserInfoVO userInfoVO = (UserInfoVO) session.get(SystemConstants.SESSION_USER_KEY);
        //获取用户信息
        UserInfo userInfo = userInfoVO.getUserInfo();
        //获取学生信息
        StudentInfo studentInfo = userInfoVO.getStudentInfo();
        GetStudentInfoVO getStudentInfoVO = BeanCopyUtils.copyFromManyBean(GetStudentInfoVO.class, userInfo, studentInfo);
        /*GetStudentInfoVO getStudentInfoVO;
        getStudentInfoVO = BeanCopyUtils.copyBean(userInfo, GetStudentInfoVO.class);
        getStudentInfoVO = BeanCopyUtils.copyBean(studentInfo, getStudentInfoVO.getClass());*/
        return getStudentInfoVO;
    }

    @Override
    public Boolean updateStudentInfo(String loginName,String headImg,String faceImg) {
//        UserInfo userInfo = new UserInfo();
//        userInfo.setLoginName(loginName);
//        userInfo.setPassword(headImg);
//        if (userInfoDao.updatePassword(userInfo) != 0 || ) {
//            return true;
//        }
        return true;
    }
}

