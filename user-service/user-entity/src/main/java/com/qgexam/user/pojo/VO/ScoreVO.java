package com.qgexam.user.pojo.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @description: 学生成绩视图对象
 * @author peter guo
 * @since 2022-01-07 16:58:24
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScoreVO implements Serializable {
    private Integer studentId;
    private String studentNumber;
    private String userName;
    private Integer finalScore;
}
