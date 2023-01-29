package com.qgexam.exam.enter.pojo.PO;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import com.qgexam.exam.enter.pojo.VO.OptionInfoVO;
import com.qgexam.exam.enter.pojo.VO.PreviewOptionInfoVO;
import com.qgexam.exam.enter.pojo.VO.PreviewSubQuestionInfoVO;
import com.qgexam.exam.enter.pojo.VO.SubQuestionInfoVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.List;

/**
 * 题目信息表(QuestionInfo)表实体类
 *
 * @author lamb007
 * @since 2023-01-09 11:44:11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionInfo implements Serializable {
    @TableId
    private Integer questionId;
    //学科编号
    private Integer subjectId;
    //章节编号
    private Integer chapterId;
    //学科名称(冗余)
    private String subjectName;
    //章节名称(冗余)
    private String chapterName;
    //题目类型(SINGLE MULTI JUDGE COMPLETION COMPLEX)
    private String type;
    //题干
    private String description;
    //题目难度等级（1-10 越大越难)
    private Integer difficultyLevel;
    //题目答案
    private String questionAns;
    //主客观类型(0sub主观题 1obj客观题)
    private Integer subOrObj;
    //是否有小题(0没有小题 1有小题)
    private Integer hasSubQuestion;
    //创建时间
    private LocalDateTime createTime;
    //更新时间
    private LocalDateTime updateTime;
    //0未删除 1已删除
    private Integer isDeleted;

    @TableField(exist = false)
    private Integer questionScore;
    // 题目选项
    @TableField(exist = false)
    private List<OptionInfoVO> optionInfo;

    // 题目小题
    @TableField(exist = false)
    private List<SubQuestionInfoVO> subQuestionInfo;

    @TableField(exist = false)
    private List<PreviewOptionInfoVO> previewOptionInfo;

    // 题目小题
    @TableField(exist = false)
    private List<PreviewSubQuestionInfoVO> previewSubQuestionInfo;

}

