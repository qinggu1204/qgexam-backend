package com.qgexam.exam.enter.pojo.PO;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
/**
 * (StudentExamAction)表实体类
 *
 * @author lamb007
 * @since 2023-01-13 15:24:29
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
    //学生切屏次数
    private Integer screenCuttingNumber;
    //创建时间
    private LocalDateTime createTime;
    //更新时间
    private LocalDateTime updateTime;
    //0未删除 1已删除
    private Integer isDeleted;

}

