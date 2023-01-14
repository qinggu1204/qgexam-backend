package com.qgexam.quartz.job;

import com.qgexam.quartz.dao.ExaminationInfoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author ljy
 * @date 2023年01月08日 23:42
 */
@Component("examUnderwayJob")
public class ExamUnderwayJob {
    @Autowired
    private ExaminationInfoDao examinationInfoDao;

    public void execute(Integer examinationId) {
        System.out.println("###########examUnderwayJob.execute()###########");
        examinationInfoDao.updateStatus(examinationId,1);
    }

}
