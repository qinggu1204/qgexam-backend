package com.qgexam.exam.viewresults.pojo.PO;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
/**
 * 小题答案明细表(SubQuestionAnswerDetail)表实体类
 *
 * @author ljy
 * @since 2023-01-10 13:03:14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubQuestionAnswerDetail implements Serializable {
    
    private Integer subQuestionAnswerDetailId;
    //答卷明细编号
    private Integer answerPaperDetailId;
    //小题编号
    private Integer subQuestionId;
    //小题答案
    private String subQuestionAnswer;
    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //0未删除 1已删除
    private Integer isDeleted;

}

