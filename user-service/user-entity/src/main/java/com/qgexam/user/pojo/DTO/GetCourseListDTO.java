package com.qgexam.user.pojo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author tageshi
 * @date 2022/12/17 21:05
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetCourseListDTO implements Serializable {
    Integer subjectId;
    String year;
    String semester;
}
