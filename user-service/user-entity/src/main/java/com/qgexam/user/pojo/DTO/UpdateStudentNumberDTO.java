package com.qgexam.user.pojo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author tageshi
 * @date 2023/1/8 21:05
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateStudentNumberDTO implements Serializable {
    Integer studentId;
    String newStudentNumber;
}
