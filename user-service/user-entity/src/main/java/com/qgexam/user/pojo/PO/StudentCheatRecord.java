package com.qgexam.user.pojo.PO;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
/**
 * (StudentCheatRecord)表实体类
 *
 * @author peter guo
 * @since 2023-01-19 00:02:12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentCheatRecord implements Serializable {
    //主键
    @TableId
    private Integer recordId;
    //考试编号
    private Integer examinationId;
    //学生编号
    private Integer studentId;
    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //0未删除 1已删除
    private Integer isDeleted;

}

