package com.qgexam.user.dao;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qgexam.user.pojo.PO.RoleInfo;
import com.qgexam.user.pojo.PO.StudentInfo;
import com.qgexam.user.pojo.PO.TeacherInfo;
import com.qgexam.user.pojo.PO.UserInfo;

import java.util.List;

/**
 * 用户信息表(UserInfo)表数据库访问层
 *
 * @author lamb007
 * @since 2022-12-10 20:12:42
 */
public interface UserInfoDao extends BaseMapper<UserInfo> {

    List<RoleInfo> selectRoleInfoListById(Integer id);



    UserInfo selectUserInfoByLoginName(String loginName);

    UserInfo getUserInfoByLoginName(String loginName);
    Integer insertTeacher(TeacherInfo teacherInfo);
    Integer insertStudent(StudentInfo studentInfo);
    Integer updatePassword(UserInfo userInfo);

    Integer updateLoginNameAndHeadImgByUserId(@Param("loginName") String loginName, @Param("headImg") String headImg, @Param("userId") Integer userId);
}

