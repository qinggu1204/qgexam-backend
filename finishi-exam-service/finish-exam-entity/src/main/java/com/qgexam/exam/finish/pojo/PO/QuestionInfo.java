package com.qgexam.exam.finish.pojo.PO;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
/**
 * 题目信息表(QuestionInfo)表实体类
 *
 * @author tageshi
 * @since 2023-01-10 19:40:40
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionInfo implements Serializable {
    
    private Integer questionId;
    //学科编号
    private Integer subjectId;
    //章节编号
    private Integer chapterId;
    //学科名称(冗余)
    private String subjectName;
    //章节名称(冗余)
    private String chapterName;
    //题目类型(SINGLE MULTI JUDGE COMPLETION COMPLEX)
    private String type;
    //题干
    private String description;
    //题目难度等级（1-10 越大越难)
    private Integer difficultyLevel;
    //题目答案
    private String questionAns;
    //主客观类型(0sub主观题 1obj客观题)
    private Integer subOrObj;
    //是否有小题(0没有小题 1有小题)
    private Integer hasSubQuestion;
    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //0未删除 1已删除
    private Integer isDeleted;

}

