package com.qgexam.user.service;

import org.apache.dubbo.config.annotation.Reference;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author tageshi
 * @date 2022/12/16 21:26
 */
@SpringBootTest
public class CourseInfoTest {
    @Reference
    CourseInfoService courseInfoService;

    /*public void getCourseListTest(){
        courseInfoService.getCourseList()
    }*/

}