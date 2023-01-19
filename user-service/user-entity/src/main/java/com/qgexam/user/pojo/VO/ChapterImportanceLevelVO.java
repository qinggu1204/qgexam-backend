package com.qgexam.user.pojo.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * 查询章节重要程度视图对象
 *
 * @author peter guo
 * @since 2022-12-23 14:11:00
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChapterImportanceLevelVO implements Serializable {
    private Integer chapterId;
    private Integer importanceLevel;

}
