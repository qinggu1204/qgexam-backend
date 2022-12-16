package com.qgexam.user.service;


import com.qgexam.user.pojo.DTO.UpdateTeacherInfoDTO;
import com.qgexam.user.pojo.DTO.UserLoginByUsernameDTO;
import com.qgexam.user.pojo.VO.UserInfoVO;

import com.qgexam.user.pojo.PO.UserInfo;

import org.apache.dubbo.config.annotation.Reference;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserInfoServiceTest {

    @Reference
    private UserInfoService userInfoService;

    @Test
    public void testUserLogin() {
        UserInfoVO userInfoVO = userInfoService.userLogin(new UserLoginByUsernameDTO("17682405206", "zhangsan"));
        System.out.println(userInfoVO);
    }
    @Test
    public void updatePasswordTest(){
        userInfoService.updatePassword("17706728821","159");
//        UserInfo userInfo=userInfoService.getUserInfoByLoginName("17706728821");
//        System.out.println(userInfo.toString());
    }
    @Test
    public void registerTeacherTest(){
        userInfoService.registerTeacher("18767172095","zqdzqd","张半蛋",
                "T06","http://xxxx",1,"浙江工业大学");
//        UserInfo userInfo=userInfoService.getUserInfoByLoginName("18767171095");
//        System.out.println(userInfo.toString());
    }
    @Test
    public void registerStudentTest(){
        userInfoService.registerStudent("19941200877","scqscq",
                "孙五","202003150417",1,"浙江工业大学");
//        UserInfo userInfo=userInfoService.getUserInfoByLoginName("19941200877");
//        System.out.println(userInfo.toString());
    }
    @Test
    public void updateTeacherInfoTest(){
        userInfoService.updateTeacherInfo(1,new UpdateTeacherInfoDTO("17682405206", "http://hhhhhh"));
    }
}
