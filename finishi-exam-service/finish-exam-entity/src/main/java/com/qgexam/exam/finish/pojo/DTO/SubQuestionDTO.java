package com.qgexam.exam.finish.pojo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author tageshi
 * @date 2023/1/9 19:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubQuestionDTO implements Serializable {
    Integer subQuestionId;
    String subQuestionAnswer;
}
