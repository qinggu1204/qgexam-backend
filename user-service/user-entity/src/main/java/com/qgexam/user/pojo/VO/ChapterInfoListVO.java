package com.qgexam.user.pojo.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author tageshi
 * @date 2022/12/17 16:55
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChapterInfoListVO implements Serializable {
    //章节编号
    Integer chapterId;
    //章节名称
    String chapterName;
    //重要程度(1-10 越大越重要)
    Integer importanceLevel;
}
