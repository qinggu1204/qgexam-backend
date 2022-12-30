package com.qgexam.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qgexam.user.pojo.PO.MessageInfo;
import com.qgexam.user.pojo.VO.MessageInfoListVO;

/**
 * 消息表(MessageInfo)表服务接口
 *
 * @author ljy
 * @since 2022-12-30 20:13:05
 */
public interface MessageInfoService extends IService<MessageInfo> {
    IPage<MessageInfoListVO> getMessageList(Integer userId, Integer currentPage, Integer pageSize);
}

