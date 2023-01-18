package com.qgexam.exam.finish.pojo.PO;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
/**
 * 考试信息表(ExaminationInfo)表实体类
 *
 * @author tageshi
 * @since 2023-01-13 17:37:23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExaminationInfo implements Serializable {
    
    private Integer examinationId;
    //试卷编号
    private Integer examinationPaperId;
    //考试名称
    private String examinationName;
    //创建人的用户编号
    private Integer createdBy;
    //考试开始时间
    private Date startTime;
    //考试结束时间
    private Date endTime;
    //考试状态(1表示进行中 2表示未开始 3表示已结束)
    private Integer status;
    //限时进入时间(0为不限时)
    private Integer limitTime;
    //是否题目乱序(0表示否，1表示是)
    private Integer isQuestionResort;
    //是否选项乱序(0表示否，1表示是)
    private Integer isOptionResort;
    //阅卷结束时间
    private Date judgeOvertime;
    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //0未删除 1已删除
    private Integer isDeleted;

}
