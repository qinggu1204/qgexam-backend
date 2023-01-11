package com.qgexam.user.pojo.PO;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
/**
 * 学科信息表(SubjectInfo)表实体类
 *
 * @author lamb007
 * @since 2023-01-11 19:37:53
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubjectInfo implements Serializable {
    @TableId
    private Integer subjectId;
    //学科名称
    private String subjectName;
    //创建时间
    private LocalDateTime createTime;
    //更新时间
    private LocalDateTime updateTime;
    //0未删除 1已删除
    private Integer isDeleted;

}

