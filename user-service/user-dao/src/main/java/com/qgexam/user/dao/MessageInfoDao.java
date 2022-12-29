package com.qgexam.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qgexam.user.pojo.PO.MessageInfo;

/**
 * 消息表(MessageInfo)表数据库访问层
 *
 * @author ljy
 * @since 2022-12-29 15:12:03
 */
public interface MessageInfoDao extends BaseMapper<MessageInfo> {
    Integer insertMessageInfo(MessageInfo messageInfo);
}

