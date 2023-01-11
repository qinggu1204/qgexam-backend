package com.qgexam.user.pojo.PO;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
/**
 * 学科章节表(ChapterInfo)表实体类
 *
 * @author peter guo
 * @since 2022-12-23 12:20:52
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChapterInfo implements Serializable {
    @TableId
    private Integer chapterId;
    //学科编号
    private Integer subjectId;
    //章节名称
    private String chapterName;
    //重要程度(1-10 越大越重要)
    private Integer importanceLevel;
    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //0未删除 1已删除
    private Integer isDeleted;

}

