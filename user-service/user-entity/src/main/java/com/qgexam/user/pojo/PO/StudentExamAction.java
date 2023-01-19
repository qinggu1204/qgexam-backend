package com.qgexam.user.pojo.PO;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
/**
 * (StudentExamAction)表实体类
 *
 * @author peter guo
 * @since 2023-01-18 11:56:12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentExamAction implements Serializable {
    //主键
    @TableId
    private Integer id;
    //学生id
    private Integer studentId;
    
    private Integer examinationId;
    //0切屏 1人脸
    private Integer type;
    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //0未删除 1已删除
    private Integer isDeleted;

}

