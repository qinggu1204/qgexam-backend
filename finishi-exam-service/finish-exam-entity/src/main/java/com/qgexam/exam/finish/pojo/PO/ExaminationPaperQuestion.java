package PO;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
/**
 * 试卷题目关系表(ExaminationPaperQuestion)表实体类
 *
 * @author tageshi
 * @since 2023-01-13 17:33:40
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExaminationPaperQuestion implements Serializable {
    
    private Integer id;
    //试卷编号
    private Integer examinationPaperId;
    //题目编号
    private Integer questionId;
    //小题分数
    private Float questionScore;
    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //0未删除 1已删除
    private Integer isDeleted;

}

