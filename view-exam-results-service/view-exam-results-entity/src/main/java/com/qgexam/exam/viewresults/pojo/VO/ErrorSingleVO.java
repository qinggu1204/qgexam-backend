package com.qgexam.exam.viewresults.pojo.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 单选题返回信息
 *
 * @author ljy
 * @since 2022-1-6 19:46:41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorSingleVO implements Serializable {
    private List<ErrorObjResultVO> singleList;
}