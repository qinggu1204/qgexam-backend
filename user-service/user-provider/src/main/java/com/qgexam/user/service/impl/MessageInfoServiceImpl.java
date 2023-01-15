package com.qgexam.user.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qgexam.user.dao.MessageInfoDao;
import com.qgexam.user.pojo.PO.MessageInfo;
import com.qgexam.user.pojo.VO.MessageInfoListVO;
import com.qgexam.user.pojo.VO.MessageInfoVO;
import com.qgexam.user.service.MessageInfoService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * 消息表(MessageInfo)表服务实现类
 *
 * @author ljy
 * @since 2022-12-30 20:13:38
 */
@DubboService
public class MessageInfoServiceImpl extends ServiceImpl<MessageInfoDao, MessageInfo> implements MessageInfoService {

    @Autowired
    private MessageInfoDao messageInfoDao;

    /**
     * @author ljy
     * @description 获取学科章节列表
     * @date 2022/12/25 20:01:03
     */
    @Override
    public IPage<MessageInfoListVO> getMessageList(Integer userId, Integer currentPage, Integer pageSize){
        IPage<MessageInfoListVO> page=new Page<>(currentPage,pageSize);
        return messageInfoDao.getMessagePage(userId,page);
    }

    @Override
    public MessageInfoVO getMessage(Integer userId, Integer messageId) {
        MessageInfoVO messageInfoVO=messageInfoDao.getMessage(userId,messageId);
        return messageInfoVO;
    }
}

