package com.qgexam.exam.viewresults.pojo.PO;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
/**
 * 错题小题信息表(SubErrorquestionInfo)表实体类
 *
 * @author ljy
 * @since 2023-01-15 11:56:17
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubErrorquestionInfo implements Serializable {
    
    private Integer subErrorquestionId;
    //学生编号
    private Integer studentId;
    //题目编号
    private Integer questionId;
    //小题编号
    private Integer subQuestionId;
    //小题题干
    private String subQuestionDesc;
    //小题答案
    private String subQuestionAns;
    //考生小题答案
    private String subQuestionAnswer;
    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //0未删除 1已删除
    private Integer isDeleted;

}

