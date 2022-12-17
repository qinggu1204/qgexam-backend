package com.qgexam.user.pojo.PO;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.List;

/**
 * 课程信息表(CourseInfo)表实体类
 *
 * @author tageshi
 * @since 2022-12-16 19:37:44
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseInfo implements Serializable {
    
    private Integer courseId;
    //课程名称
    private String courseName;
    //课程封面url
    private String courseUrl;
    
    private Integer subjectId;
    
    private String subjectName;
    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //0未删除 1已删除
    private Integer isDeleted;
    //学年
    private String year;
    //学期
    private String semester;

    //课程教师列表
    @TableField(exist = false)
    private List<TeacherInfo> teacherList;

}

