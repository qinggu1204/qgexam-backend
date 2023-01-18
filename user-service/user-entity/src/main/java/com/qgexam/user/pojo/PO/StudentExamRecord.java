package com.qgexam.user.pojo.PO;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
/**
 * 考试信息表(StudentExamRecord)表实体类
 *
 * @author peter guo
 * @since 2023-01-18 16:20:29
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
    
    private String remoteIp;
    //是否作弊
    private Integer isCheat;
    //进入时间
    private Date enterTime;
    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //0未删除 1已删除
    private Integer isDeleted;

}

