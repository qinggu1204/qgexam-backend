package com.qgexam.exam.enter.job;

import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author yzw
 * @date 2023年01月08日 11:08
 */
@Component("testJob")
public class TestJob {
    public void execute() {
        System.out.println("==========考试开始========== " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    }
}
