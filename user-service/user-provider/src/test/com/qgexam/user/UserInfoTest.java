package com.qgexam.user;

import com.qgexam.user.dao.UserInfoDao;
import org.junit.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author yzw
 * @description UserInfo单元测试
 * @date 2022/12/14 12:19:23
 */
@SpringBootTest(classes = UserProviderApplication.class)
public class UserInfoTest {

    @Autowired
    private UserInfoDao userInfoDao;
    @Test
    public void testSelectRoleListById() {
        List<String> strings = userInfoDao.selectRoleListById(1);
        System.out.println(strings);
    }
}
