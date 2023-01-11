package com.qgexam.user.pojo.PO;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
/**
 * 考试信息表(ExaminationInfo)表实体类
 *
 * @author peter guo
 * @since 2022-12-25 22:51:36
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExaminationInfo implements Serializable {
    @TableId
    private Integer examinationId;
    //试卷编号
    private Integer examinationPaperId;
    //考试名称
    private String examinationName;
    
    private Integer createdBy;
    //考试结束时间
    private Date endTime;
    //考试状态(NOT_START UNDERWAY OVER)
    private String status;
    //考试开始时间
    private Date startTime;
    //是否题目乱序(0表示否，1表示是)
    private Integer isQuestionResort;
    //是否选项乱序(0表示否，1表示是)
    private Integer isOptionResort;
    //限时进入时间(0为不限时)
    private Integer limitTime;
    //更新时间
    private Date updateTime;
    //0未删除 1已删除
    private Integer isDeleted;
    //创建时间
    private Date createTime;

}

