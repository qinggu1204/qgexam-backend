package com.qgexam.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qgexam.user.pojo.PO.MessageInfo;
import com.qgexam.user.pojo.VO.MessageInfoListVO;
import com.qgexam.user.pojo.VO.MessageInfoVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 消息表(MessageInfo)表数据库访问层
 *
 * @author ljy
 * @since 2022-12-29 15:12:03
 */
public interface MessageInfoDao extends BaseMapper<MessageInfo> {
    Integer insertMessageInfo(MessageInfo messageInfo);
    IPage<MessageInfoListVO> getMessagePage(@Param("userId") Integer userId, IPage<MessageInfoListVO> page);
    MessageInfoVO getMessage(@Param("userId") Integer userId,@Param("messageId") Integer messageId);
    Integer insertMarkingMessageBatch(@Param("messageInfoList") List<MessageInfo> messageInfoList);

    Integer insertScoreQueryMessageBatch(@Param("messageInfoList") List<MessageInfo> messageInfoList);

    Integer updateStatus(Integer messageId);
}

