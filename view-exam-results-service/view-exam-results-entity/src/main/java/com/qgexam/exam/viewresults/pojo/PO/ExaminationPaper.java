package com.qgexam.exam.viewresults.pojo.PO;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.List;

/**
 * 试卷信息表(ExaminationPaper)表实体类
 *
 * @author ljy
 * @since 2023-01-12 15:50:32
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExaminationPaper implements Serializable {
    
    private Integer examinationPaperId;
    //试卷标题
    private String title;
    //试卷总分
    private Integer totalScore;
    //创建人的用户编号
    private Integer createdBy;
    //更新时间
    private Date updateTime;
    //0未删除 1已删除
    private Integer isDeleted;
    //创建时间
    private Date createTime;

    @TableField(exist = false)
    private List<QuestionInfo> questionInfoList;
}

