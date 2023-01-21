package com.qgexam.exam.enter.PO;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
/**
 * (StudentCheatRecord)表实体类
 *
 * @author lamb007
 * @since 2023-01-21 00:39:20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentCheatRecord implements Serializable {
    //主键
    private Integer recordId;
    //考试编号
    private Integer examinationId;
    //学生编号
    private Integer studentId;
    //创建时间
    private LocalDateTime createTime;
    //更新时间
    private LocalDateTime updateTime;
    //0未删除 1已删除
    private Integer isDeleted;

}

