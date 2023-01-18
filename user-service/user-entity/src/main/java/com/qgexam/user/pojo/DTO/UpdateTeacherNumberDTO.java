package com.qgexam.user.pojo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author tageshi
 * @date 2023/1/8 22:13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTeacherNumberDTO implements Serializable {
    Integer teacherId;
    String newTeacherNumber;
}
