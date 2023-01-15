package com.qgexam.quartz.job;

import com.qgexam.quartz.dao.ExaminationInfoDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author ljy
 * @date 2023年01月08日 23:42
 */
@Slf4j
@Component("examUnderwayJob")
public class ExamUnderwayJob {
    @Autowired
    private ExaminationInfoDao examinationInfoDao;

    public void execute(Integer examinationId) {
        log.info("###########examUnderwayJob.execute()###########");
        examinationInfoDao.updateStatus(examinationId,1);
    }

}
