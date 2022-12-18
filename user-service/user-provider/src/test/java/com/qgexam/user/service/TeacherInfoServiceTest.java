package com.qgexam.user.service;

import com.qgexam.user.pojo.DTO.CreateCourseDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.Reference;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TeacherInfoServiceTest {
    @DubboReference
    private TeacherInfoService teacherInfoService;

    @Test
    public void testCreateCourse() {
        teacherInfoService.createCourse(3, "李伟", new CreateCourseDTO(
                1, "操作系统原理", "李伟的操作系统课", "http://www.baidu.com",
                "2022-2023", "第一学期"));

    }

}
