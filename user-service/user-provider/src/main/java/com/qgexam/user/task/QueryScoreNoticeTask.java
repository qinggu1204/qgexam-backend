package com.qgexam.user.task;

import com.qgexam.common.core.constants.ExamConstants;
import com.qgexam.common.core.constants.JobConstants;
import com.qgexam.common.core.constants.MessageConstants;
import com.qgexam.common.core.constants.SystemConstants;
import com.qgexam.common.core.utils.QuartzManager;
import com.qgexam.common.redis.utils.RedisCache;
import com.qgexam.user.dao.AnswerPaperInfoDao;
import com.qgexam.user.dao.MessageInfoDao;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class QueryScoreNoticeTask implements Job {
    @Autowired
    private RedisCache redisCache;

    @Autowired
    private MessageInfoDao messageInfoDao;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //将消息编号列表从redis中取出
        List<Integer> messageIdList = redisCache.getCacheList(MessageConstants.MESSAGE_ID_LIST_KEY);
        //更新is_delete，使消息可以查询到
        messageInfoDao.updateBatch(messageIdList);
        redisCache.deleteObject(MessageConstants.MESSAGE_ID_LIST_KEY);

        //将答卷编号列表从redis中取出
        List<Integer> answerPaperIdList = redisCache.getCacheList(ExamConstants.ANSWER_PAPER_ID_LIST_KEY);
        answerPaperIdList.forEach(answerPaperId ->{
            redisCache.deleteObject(ExamConstants.ANSWER_PAPER_KEY + answerPaperId);
        });
        redisCache.deleteObject(ExamConstants.ANSWER_PAPER_ID_LIST_KEY);

        QuartzManager.removeJob(JobConstants.QUERY_SCORE_NOTICE_JOB_NAME, JobConstants.JOB_GROUP_NAME,
                JobConstants.QUERY_SCORE_NOTICE_TRIGGER_NAME, JobConstants.TRIGGER_GROUP_NAME);
    }
}
