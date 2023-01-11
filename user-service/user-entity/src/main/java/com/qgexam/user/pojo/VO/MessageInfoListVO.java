package com.qgexam.user.pojo.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author ljy
 * @date 2022/12/30 16:55
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageInfoListVO implements Serializable {
    //消息编号
    Integer messageId;
    //消息标题
    String title;
    //消息状态
    Integer status;
}
