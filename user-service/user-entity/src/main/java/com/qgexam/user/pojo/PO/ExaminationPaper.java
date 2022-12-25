package com.qgexam.user.pojo.PO;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
/**
 * 试卷信息表(ExaminationPaper)表实体类
 *
 * @author tageshi
 * @since 2022-12-22 15:03:24
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
    
    private Integer createdBy;
    //更新时间
    private Date updateTime;
    //0未删除 1已删除
    private Integer isDeleted;
    //创建时间
    private Date createTime;

}

