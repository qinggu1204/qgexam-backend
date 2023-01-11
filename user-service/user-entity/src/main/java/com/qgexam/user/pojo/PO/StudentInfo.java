package com.qgexam.user.pojo.PO;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
/**
 * 学生表(StudentInfo)表实体类
 *
 * @author peter guo
 * @since 2022-12-15 14:27:19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentInfo implements Serializable {
    //学生编号
    @TableId
    private Integer studentId;
    //用户编号
    private Integer userId;
    //姓名(用户名)
    private String userName;
    //学号
    private String studentNumber;
    //学校编号
    private Integer schoolId;
    //学校名称(冗余)
    private String schoolName;
    //人脸url
    private String faceImg;
    //人脸特征
    private byte[] faceFeature;
    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //0未删除 1已删除
    private Integer isDeleted;

}

