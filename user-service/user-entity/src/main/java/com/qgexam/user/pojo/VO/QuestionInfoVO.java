package com.qgexam.user.pojo.VO;

import com.baomidou.mybatisplus.annotation.TableField;
import com.qgexam.user.pojo.PO.OptionInfo;
import com.qgexam.user.pojo.PO.SubQuestionInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author yzw
 * @date 2023年01月09日 15:25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionInfoVO implements Serializable {

    private Integer questionId;
    //题目类型(SINGLE MULTI JUDGE COMPLETION COMPLEX)
    private String type;
    //题干
    private String description;

    // 题目分数，平均算
    private Integer questionScore;
    // 题目选项
    private List<OptionInfo> optionInfo;

    // 题目小题
    private List<SubQuestionInfo> subQuestionInfo;


}


