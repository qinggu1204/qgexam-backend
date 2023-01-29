package com.qgexam.quartz.job;

import com.qgexam.common.core.constants.ExamConstants;
import com.qgexam.common.redis.utils.RedisCache;
import com.qgexam.quartz.dao.SysJobDao;
import com.qgexam.rabbit.constants.BeginCacheRabbitConstants;
import com.qgexam.rabbit.service.RabbitService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component("beginCacheJob")
@Transactional
public class BeginCacheJob {

    @Autowired
    private RedisCache redisCache;

    @DubboReference(registry = "rabbitmqRegistry")
    private RabbitService rabbitService;

    @Autowired
    private SysJobDao sysJobDao;

    @Transactional(rollbackFor = Exception.class)
    public void execute(Integer examinationId) {
        rabbitService.sendMessage(BeginCacheRabbitConstants.BEGIN_CACHE_EXCHANGE_NAME,
                BeginCacheRabbitConstants.BEGIN_CACHE_ROUTING_KEY, examinationId);
    }
}
