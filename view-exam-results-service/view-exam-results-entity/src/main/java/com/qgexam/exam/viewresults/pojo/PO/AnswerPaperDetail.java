package com.qgexam.exam.viewresults.pojo.PO;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
/**
 * 答卷明细表(AnswerPaperDetail)表实体类
 *
 * @author ljy
 * @since 2022-12-21 19:40:43
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnswerPaperDetail implements Serializable {
    
    private Integer answerPaperDetailId;
    //答卷编号
    private Integer answerPaperId;
    //题目编号
    private Integer questionId;
    //考生答案
    private String studentAnswer;
    //得分(默认0)
    private Integer score;
    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //0未删除 1已删除
    private Integer isDeleted;

}

