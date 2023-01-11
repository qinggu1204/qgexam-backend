package com.qgexam.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qgexam.user.pojo.PO.MessageInfo;

/**
 * 消息表(MessageInfo)表服务接口
 *
 * @author peter guo
 * @since 2023-01-06 21:18:23
 */
public interface MessageInfoService extends IService<MessageInfo> {

    Integer getBadgeNumber(Integer userId);
}

