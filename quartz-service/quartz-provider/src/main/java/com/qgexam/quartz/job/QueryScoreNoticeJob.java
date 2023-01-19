package com.qgexam.quartz.job;

import com.qgexam.common.core.constants.MessageConstants;
import com.qgexam.common.redis.utils.RedisCache;
import com.qgexam.quartz.dao.MessageInfoDao;
import com.qgexam.quartz.dao.SysJobDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component("queryScoreNoticeJob")
@Transactional
public class QueryScoreNoticeJob {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private MessageInfoDao messageInfoDao;

    @Autowired
    private SysJobDao sysJobDao;

    @Transactional(rollbackFor = Exception.class)
    public void execute() {
        //将消息编号列表从redis中取出
        List<Integer> messageIdList = redisCache.getCacheList(MessageConstants.MESSAGE_ID_LIST_KEY);
        //更新is_delete，使消息可以查询到
        messageInfoDao.updateBatch(messageIdList);
        redisCache.deleteObject(MessageConstants.MESSAGE_ID_LIST_KEY);
    }
}
