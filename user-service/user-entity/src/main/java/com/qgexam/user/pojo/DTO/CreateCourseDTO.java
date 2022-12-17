package com.qgexam.user.pojo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCourseDTO implements Serializable {

    @NotNull(message = "学科编号不能为空")
    private Integer subjectId;

    @NotBlank(message = "学科名称不能为空")
    private String subjectName;

    @NotBlank(message = "课程名称不能为空")
    private String courseName;

    @NotBlank(message = "课程封面url不能为空")
    private String courseUrl;

    @NotBlank(message = "学年不能为空")
    private String yearName;

    @NotBlank(message = "学期不能为空")
    private String semesterName;
}
