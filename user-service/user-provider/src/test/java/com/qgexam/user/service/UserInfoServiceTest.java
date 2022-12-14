package com.qgexam.user.service;

import com.qgexam.user.pojo.DTO.TeacherRegisterDTO;
import com.qgexam.user.pojo.PO.UserInfo;
import org.apache.dubbo.config.annotation.Reference;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserInfoServiceTest {

    @Reference
    private UserInfoService userInfoService;

    @Test
    public void getUserInfoByIdTest() {
        System.out.println(userInfoService.getUserInfoById(1));
    }

    @Test
    public void teacherRegister(){
        TeacherRegisterDTO teacherRegisterDTO=new TeacherRegisterDTO();
        teacherRegisterDTO.setUserName("ÕÅÈ«µ°");
        teacherRegisterDTO.setPassword("666888");
        teacherRegisterDTO.setRePassword("666888");
        teacherRegisterDTO.setPhoneNumber("18767171095");
        teacherRegisterDTO.setTeacherNumber("T06");
        teacherRegisterDTO.setSchoolId(1);
        teacherRegisterDTO.setQualificationImg("http://xxxx");
        userInfoService.registerTeacher(teacherRegisterDTO);
        UserInfo userInfo=userInfoService.getUserInfoByLoginName("18767171095");
        System.out.println(userInfo.toString());
    }
}
