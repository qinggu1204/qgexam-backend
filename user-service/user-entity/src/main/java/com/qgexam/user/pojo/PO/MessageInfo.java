package com.qgexam.user.pojo.PO;

import java.time.LocalDateTime;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
/**
 * 消息表(MessageInfo)表实体类
 *
 * @author ljy
 * @since 2022-12-29 15:09:12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageInfo implements Serializable {
    //消息编号
    @TableId
    private Integer messageId;
    //用户编号
    private Integer userId;
    //消息标题（考试通知、监考通知、阅卷通知）
    private String title;
    //消息状态，0表示未读，1表示已读
    private Integer status;
    //考试名称
    private String examinationName;
    //考试/阅卷开始时间
    private LocalDateTime startTime;
    //考试/阅卷结束时间
    private LocalDateTime endTime;
    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //0未删除，1已删除
    private Integer isDeleted;

}

