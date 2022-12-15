package com.qgexam.user.service;

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
    public void updatePasswordTest(){
        userInfoService.updatePassword("17706728821","159");
        UserInfo userInfo=userInfoService.getUserInfoByLoginName("17706728821");
        System.out.println(userInfo.toString());
    }
    @Test
    public void registerTeacherTest(){
        userInfoService.registerTeacher("18767172095","zqdzqd","张半蛋",
                "T06","http://xxxx",1,"浙江工业大学");
        UserInfo userInfo=userInfoService.getUserInfoByLoginName("18767171095");
        System.out.println(userInfo.toString());
    }
    @Test
    public void registerStudentTest(){
        userInfoService.registerStudent("19941200877","scqscq",
                "孙五","202003150417",1,"浙江工业大学");
        UserInfo userInfo=userInfoService.getUserInfoByLoginName("19941200877");
        System.out.println(userInfo.toString());
    }
}
