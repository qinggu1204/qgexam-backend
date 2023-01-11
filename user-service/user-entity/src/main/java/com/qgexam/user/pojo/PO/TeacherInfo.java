package com.qgexam.user.pojo.PO;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.List;

/**
 * 教师表(TeacherInfo)表实体类
 * @author tageshi
 * @since 2022-12-14 19:40:13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherInfo implements Serializable {
    //教师编号
    @TableId
    private Integer teacherId;
    //用户编号
    private Integer userId;
    //工号
    private String teacherNumber;
    //教师资格证url
    private String qualificationImg;
    //姓名(用户名)
    private String userName;
    //学校编号
    private Integer schoolId;
    //学校名称(冗余)
    private String schoolName;
    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //0未删除 1已删除
    private Integer isDeleted;

    @TableField(exist = false)
    private List<CourseInfo> courseList;

}

