package com.qgexam.user.pojo.PO;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
/**
 * 选项表(OptionInfo)表实体类
 *
 * @author lamb007
 * @since 2023-01-09 11:47:04
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
    private LocalDateTime createTime;
    //更新时间
    private LocalDateTime updateTime;
    //0未删除 1已删除
    private Integer isDeleted;

}

