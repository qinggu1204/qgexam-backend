package com.qgexam.exam.viewresults.pojo.PO;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
/**
 * 答卷表(AnswerPaperInfo)表实体类
 *
 * @author ljy
 * @since 2022-12-21 19:40:26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnswerPaperInfo implements Serializable {
    
    private Integer answerPaperId;
    //教师编号
    private Integer teacherId;
    //考试记录编号
    private Integer recordId;
    //客观题得分
    private Integer objectiveScore;
    //主观题得分
    private Integer subjectiveScore;
    //卷面总分
    private Integer paperTotalSccore;
    //考试编号
    private Integer examinationId;
    
    private String examinationName;
    //是否已批阅
    private String isMarked;
    //学生编号
    private Integer studentId;
    //更新时间
    private Date updateTime;
    //0未删除 1已删除
    private Integer isDeleted;
    //学生是否可以查询答卷(0 不可以 1可以)
    private Integer canQuery;
    //创建时间
    private Date createTime;

}

