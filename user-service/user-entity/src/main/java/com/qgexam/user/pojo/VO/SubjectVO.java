package com.qgexam.user.pojo.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 查询学生信息接口视图对象
 *
 * @author peter guo
 * @since 2022-12-24 17:16:55
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubjectVO implements Serializable {
    private Integer subjectId;
    //学科名称
    private String subjectName;
}
