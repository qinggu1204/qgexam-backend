package com.qgexam.exam.enter.pojo.PO;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
/**
 * 考试信息表(StudentExamRecord)表实体类
 *
 * @author lamb007
 * @since 2023-01-10 15:02:37
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentExamRecord implements Serializable {
    @TableId
    private Integer recordId;
    //考试编号
    private Integer examinationId;
    //学生编号
    private Integer studentId;
    // 客户端ip
    private String remoteIp;
    //进入时间
    private LocalDateTime enterTime;
    //创建时间
    private LocalDateTime createTime;
    //更新时间
    private LocalDateTime updateTime;
    //0未删除 1已删除
    private Integer isDeleted;

}

