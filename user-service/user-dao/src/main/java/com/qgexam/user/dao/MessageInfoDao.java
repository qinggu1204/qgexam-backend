package com.qgexam.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qgexam.user.pojo.PO.MessageInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 消息表(MessageInfo)表数据库访问层
 *
 * @author peter guo
 * @since 2023-01-03 19:49:27
 */
public interface MessageInfoDao extends BaseMapper<MessageInfo> {

    Integer insertMarkingMessageBatch(@Param("messageInfoList") List<MessageInfo> messageInfoList);

    Integer insertScoreQueryMessageBatch(@Param("messageInfoList") List<MessageInfo> messageInfoList);

    Integer updateBatch(@Param("messageIdList") List<Integer> messageIdList);
}

