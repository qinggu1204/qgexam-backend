package com.qgexam;


import com.qgexam.exam.finish.dao.AnswerPaperDetailDao;

import com.qgexam.exam.finish.service.FinishExamProviderApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = FinishExamProviderApplication.class)
public class AppTest {

    @Autowired
    private AnswerPaperDetailDao answerPaperDetailDao;
    @Test
    public void shouldAnswerWithTrue() {
        Integer answerPaperDetailId = answerPaperDetailDao.getAnswerPaperDetailId(1, 1);
        System.out.println("answerPaperDetailId: " + answerPaperDetailId);

    }
}
