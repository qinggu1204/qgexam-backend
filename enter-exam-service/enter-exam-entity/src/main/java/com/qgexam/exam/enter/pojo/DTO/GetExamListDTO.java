package com.qgexam.exam.enter.pojo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author yzw
 * @date 2023年01月06日 16:21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetExamListDTO implements Serializable{

    private Integer courseId;
    private String examinationName;
    private Integer currentPage;
    private Integer pageSize;
    private Integer studentId;

}
