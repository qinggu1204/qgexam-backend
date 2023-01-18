package com.qgexam.quartz.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qgexam.user.pojo.PO.MessageInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 消息表(MessageInfo)表数据库访问层
 *
 * @author peter guo
 * @since 2023-01-14 20:28:30
 */
@Mapper
public interface MessageInfoDao extends BaseMapper<MessageInfo> {
    Integer updateBatch(@Param("messageIdList") List<Integer> messageIdList);

}

