package com.qgexam.user.service;

import com.qgexam.user.pojo.PO.UserInfo;
import org.junit.Test;
import org.springframework.security.core.userdetails.User;

import java.util.*;

/**
 * @author yzw
 * @date 2023年01月10日 16:47
 */

public class TimeTest {

    @Test
    public void test() {
        UserInfo userInfo1 = new UserInfo();
        userInfo1.setUserId(1);

        UserInfo userInfo2 = new UserInfo();
        userInfo2.setUserId(2);

        UserInfo userInfo3 = new UserInfo();
        userInfo3.setUserId(3);

        UserInfo userInfo4 = new UserInfo();
        userInfo4.setUserId(4);

        UserInfo userInfo5 = new UserInfo();
        userInfo5.setUserId(5);
        List<UserInfo> list = Arrays.asList(
                userInfo1, userInfo2, userInfo3, userInfo4, userInfo5
        );
        for (int i = 0; i < 5; i++) {
            Collections.shuffle(list);
            list.stream()
                    .forEach(userInfo -> System.out.print(userInfo.getUserId() + " "));
            System.out.println();
        }

    }
}
