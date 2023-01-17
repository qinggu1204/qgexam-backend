package com.qgexam.exam.finish.pojo.PO;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
/**
 * (ExamSubmitRecord)表实体类
 *
 * @author tageshi
 * @since 2023-01-13 18:00:54
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamSubmitRecord implements Serializable {
    //提交记录id
    private Integer id;
    //学生id
    private Integer studentId;
    //提交时间
    private Date submitTime;
    
    private Date createTime;
    //更新时间
    private Date updateTime;
    //0未删除 1已删除
    private Integer isDeleted;

}

