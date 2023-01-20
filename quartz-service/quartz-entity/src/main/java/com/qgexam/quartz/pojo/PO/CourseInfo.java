package com.qgexam.quartz.pojo.PO;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
/**
 * 课程信息表(CourseInfo)表实体类
 *
 * @author lamb007
 * @since 2023-01-20 10:15:40
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseInfo implements Serializable {
    //主键
    @TableId
    private Integer courseId;
    //课程名称
    private String courseName;
    //课程封面url
    private String courseUrl;
    //学科编号
    private Integer subjectId;
    //学科名称
    private String subjectName;
    //创建时间
    private LocalDateTime createTime;
    //更新时间
    private LocalDateTime updateTime;
    //0未删除 1已删除
    private Integer isDeleted;
    //学年
    private String year;
    //学期
    private String semester;

}

