package com.qgexam.user.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qgexam.user.dao.ChapterInfoDao;
import com.qgexam.user.pojo.DTO.CreatePaperDTO;
import com.qgexam.user.pojo.PO.ChapterInfo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author peter guo
 * @description NeTeacherInfoServiceTest
 * @date 2022/12/24 15:58:24
 */
@SpringBootTest
public class NeTeacherInfoServiceTest {
    @DubboReference
    private NeTeacherInfoService neTeacherInfoService;

    @Test
    public void createPaperTest(){
        CreatePaperDTO createPaperDTO = new CreatePaperDTO();
        createPaperDTO.setSubjectId(1);
        List<Integer> chapterList = new ArrayList<>();
        chapterList.add(1);
        chapterList.add(2);
        chapterList.add(3);
        createPaperDTO.setChapterList(chapterList);
        neTeacherInfoService.createPaper(1,createPaperDTO);
    }

    @Test
    public void roundTest() {
        int round = (int) Math.round(0.29166666666666666666666666666667*10);
        int round1 = (int) Math.round(0.33333333333333333333333333333333*10);
        int round2 = 10-round-round1;
        System.out.println(round);
        System.out.println(round1);
        System.out.println(round2);
        Assertions.assertEquals(1,round+round1+round2,"方法测试不通过");
    }
}
