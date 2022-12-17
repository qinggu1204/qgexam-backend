package com.qgexam.user.pojo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author tageshi
 * @date 2022/12/16 19:52
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetCourseListDTO {
    /*筛选条件*/
    Integer subjectId;
    String year;
    String semester;
}
