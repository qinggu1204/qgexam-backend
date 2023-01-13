package com.qgexam.exam.viewresults.pojo.PO;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
/**
 * 选项表(OptionInfo)表实体类
 *
 * @author ljy
 * @since 2023-01-10 12:55:43
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OptionInfo implements Serializable {
    
    private Integer optionId;
    //题目编号
    private Integer questionId;
    //选项名称
    private String optionName;
    //选项描述
    private String optionDesc;
    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //0未删除 1已删除
    private Integer isDeleted;

}

