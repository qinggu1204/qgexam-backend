package com.qgexam.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qgexam.user.dao.MessageInfoDao;
import com.qgexam.user.pojo.PO.MessageInfo;
import com.qgexam.user.service.MessageInfoService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 消息表(MessageInfo)表服务实现类
 *
 * @author peter guo
 * @since 2023-01-06 21:19:16
 */
@DubboService
public class MessageInfoServiceImpl extends ServiceImpl<MessageInfoDao, MessageInfo> implements MessageInfoService {

    @Autowired
    private MessageInfoDao messageInfoDao;

    @Override
    public Integer getBadgeNumber(Integer userId) {
        LambdaQueryWrapper<MessageInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MessageInfo::getUserId, userId)
                .eq(MessageInfo::getStatus, 0);
        return messageInfoDao.selectCount(queryWrapper).intValue();
    }
}

