package com.qgexam.exam.finish.pojo.PO;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
/**
 * 小题答案明细表(SubQuestionAnswerDetail)表实体类
 *
 * @author tageshi
 * @since 2023-01-10 16:40:50
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubQuestionAnswerDetail implements Serializable {

    private Integer subQuestionAnswerDetailId;
    @TableId("answer_paper_detail_id")
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

