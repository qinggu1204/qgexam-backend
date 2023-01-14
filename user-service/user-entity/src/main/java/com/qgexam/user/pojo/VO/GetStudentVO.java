package com.qgexam.user.pojo.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author tageshi
 * @date 2023/1/8 14:19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetStudentVO implements Serializable {
    String studentNumber;
    String userName;
}
