package com.qgexam.exam.enter.pojo.PO;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
/**
 * 小题信息表(SubQuestionInfo)表实体类
 *
 * @author lamb007
 * @since 2023-01-09 11:46:43
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubQuestionInfo implements Serializable {
    
    private Integer subQuestionId;
    //题目编号
    private Integer questionId;
    //小题题干
    private String subQuestionDesc;
    //小题答案
    private String subQuestionAns;
    //创建时间
    private LocalDateTime createTime;
    //更新时间
    private LocalDateTime updateTime;
    //0未删除 1已删除
    private Integer isDeleted;

}

