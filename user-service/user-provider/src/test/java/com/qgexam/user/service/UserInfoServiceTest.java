package com.qgexam.user.service;

import com.qgexam.user.pojo.DTO.UserLoginByUsernameDTO;
import com.qgexam.user.pojo.VO.UserInfoVO;
import org.apache.dubbo.config.annotation.Reference;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author peter guo
 * @description UserInfoServiceTest
 * @date 2022/12/14 19:20:04
 */
@SpringBootTest
public class UserInfoServiceTest {

    @Reference
    private UserInfoService userInfoService;

    @Test
    public void getUserInfoTest() {
        //System.out.println(userInfoService.getUserInfo());
    }

    @Test
    public void testUserLogin() {
        UserInfoVO userInfoVO = userInfoService.userLogin(new UserLoginByUsernameDTO("17682405206", "zhangsan"));
        System.out.println(userInfoVO);
    }

    @Test
    public void updatePasswordTest() {
        userInfoService.updatePassword("17706728821", "159");
//        UserInfo userInfo=userInfoService.getUserInfoByLoginName("17706728821");
//        System.out.println(userInfo.toString());
    }

    @Test
    public void registerTeacherTest() {
        userInfoService.registerTeacher("18767172095", "zqdzqd", "张半蛋",
                "T06", "http://xxxx", 1, "浙江工业大学");
//        UserInfo userInfo=userInfoService.getUserInfoByLoginName("18767171095");
//        System.out.println(userInfo.toString());
    }

    @Test
    public void registerStudentTest() {
        userInfoService.registerStudent("19941200877", "scqscq",
                "孙五", "202003150417", 1, "浙江工业大学");
//        UserInfo userInfo=userInfoService.getUserInfoByLoginName("19941200877");
//        System.out.println(userInfo.toString());
    }

}
