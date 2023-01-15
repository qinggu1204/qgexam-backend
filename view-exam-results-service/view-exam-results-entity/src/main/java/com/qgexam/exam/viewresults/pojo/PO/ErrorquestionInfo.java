package com.qgexam.exam.viewresults.pojo.PO;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
/**
 * 错题信息表(ErrorquestionInfo)表实体类
 *
 * @author ljy
 * @since 2023-01-15 00:35:25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorquestionInfo implements Serializable {
    
    private Integer errorquestionId;
    //学生编号
    private Integer studentId;
    //题目编号
    private Integer questionId;
    //学科编号
    private Integer subjectId;
    //题目类型(SINGLE MULTI JUDGE COMPLETION COMPLEX)
    private String type;
    //题干
    private String description;
    //章节名称
    private String chapterName;
    //题目难度等级（1-10 越大越难)
    private Integer difficultyLevel;
    //题目分数
    private Integer questionScore;
    //学生得分(默认0)
    private Integer score;
    //题目答案
    private String questionAns;
    //考生答案
    private String studentAnswer;
    //是否有小题(0没有小题 1有小题)
    private Integer hasSubQuestion;
    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //0未删除 1已删除
    private Integer isDeleted;

}

