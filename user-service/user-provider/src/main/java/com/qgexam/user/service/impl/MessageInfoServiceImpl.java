package com.qgexam.user.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qgexam.common.core.api.AppHttpCodeEnum;
import com.qgexam.common.core.exception.BusinessException;
import com.qgexam.user.dao.MessageInfoDao;
import com.qgexam.user.pojo.PO.MessageInfo;
import com.qgexam.user.pojo.VO.MessageInfoListVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qgexam.user.dao.MessageInfoDao;
import com.qgexam.user.pojo.PO.MessageInfo;
import com.qgexam.user.pojo.VO.MessageInfoVO;
import com.qgexam.user.service.MessageInfoService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.ZoneId;
import java.util.Date;


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
        MessageInfo messageInfo=messageInfoDao.getMessage(userId,messageId);
        MessageInfoVO messageInfoVO=new MessageInfoVO();
        messageInfoVO.setExaminationName(messageInfo.getExaminationName());
        messageInfoVO.setStartTime(messageInfo.getStartTime());
        messageInfoVO.setEndTime(messageInfo.getEndTime());
        messageInfoDao.updateStatus(messageId);
        return messageInfoVO;
    }

    @Override
    public void deleteMessage(Integer messageId) {
        int deleteCount = messageInfoDao.deleteById(messageId);
        if (deleteCount == 0) {
            throw new BusinessException(AppHttpCodeEnum.SYSTEM_ERROR.getCode(), "删除失败");
        }
    }

    @Override
    public Integer getBadgeNumber(Integer userId) {
        LambdaQueryWrapper<MessageInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MessageInfo::getUserId, userId)
                .eq(MessageInfo::getStatus, 0);
        return messageInfoDao.selectCount(queryWrapper).intValue();
    }
}

