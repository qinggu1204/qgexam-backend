package com.qgexam.user.pojo.PO;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
/**
 * 学校表(SchoolInfo)表实体类
 *
 * @author peter guo
 * @since 2022-12-14 11:12:07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SchoolInfo implements Serializable {
    //学校编号
    @TableId
    private Integer schoolId;
    //学校名称
    private String schoolName;
    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //0未删除 1已删除
    private Integer isDeleted;

}

