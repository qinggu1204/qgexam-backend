package com.qgexam.user.service;

import com.qgexam.user.dao.QuestionInfoDao;
import com.qgexam.user.dao.SubQuestionInfoDao;
import com.qgexam.user.dao.SubjectInfoDao;
import com.qgexam.user.pojo.PO.QuestionInfo;
import com.qgexam.user.pojo.PO.SubQuestionInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author yzw
 * @date 2023年01月11日 19:40
 */
@SpringBootTest
public class TranTest {
    @Autowired
    private QuestionInfoDao questionInfoDao;

    @Autowired
    private SubQuestionInfoDao subQuestionInfoDao;

    @Test
    @Transactional
    public void test(){
        QuestionInfo questionInfo = new QuestionInfo();
        questionInfo.setDescription("事务测试问题");
        int count1 = questionInfoDao.insert(questionInfo);
        System.out.println(count1);
//        SubQuestionInfo subQuestionInfo = new SubQuestionInfo();
//        subQuestionInfo.setSubQuestionDesc("测试问题的小题");
//        int count2 = subQuestionInfoDao.insert(subQuestionInfo);
//        System.out.println(count2);
    }
}
