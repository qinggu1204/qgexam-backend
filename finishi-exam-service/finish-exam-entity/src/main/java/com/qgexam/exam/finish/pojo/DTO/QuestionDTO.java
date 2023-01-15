package com.qgexam.exam.finish.pojo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author tageshi
 * @date 2023/1/9 19:07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionDTO implements Serializable {
    Integer questionId;
    String questionAnswer;
    List<SubQuestionDTO>subQuestion;
}
