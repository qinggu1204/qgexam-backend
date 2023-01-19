package com.qgexam.user.pojo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JoinCourseDTO {
    @NotNull(message = "课程编号不能为空")
    @Digits(integer = 11,fraction = 0,message = "课程编号必须为大于0的数字")
    @Min(value = 1,message = "课程编号必须为大于0的数字")
    private Integer courseId;
}
