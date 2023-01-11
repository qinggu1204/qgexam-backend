package com.qgexam.user.pojo.PO;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
/**
 * 试卷信息表(ExaminationPaper)表实体类
 *
 * @author peter guo
 * @since 2022-12-23 12:10:26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExaminationPaper implements Serializable {
    @TableId
    private Integer examinationPaperId;
    //试卷标题
    private String title;
    //试卷总分
    private Integer totalScore;
    //创建人
    private Integer createdBy;
    //更新时间
    private Date updateTime;
    //0未删除 1已删除
    private Integer isDeleted;
    //创建时间
    private Date createTime;

}

