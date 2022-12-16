package com.qgexam.common.web.handler.exception;


import cn.hutool.core.exceptions.ExceptionUtil;

import com.qgexam.common.core.api.AppHttpCodeEnum;
import com.qgexam.common.core.api.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @project qgexam
 * @description 处理其他异常
 * @author yzw
 * @date 2022/12/16 09:45:57
 * @version 1.0
 */
@RestControllerAdvice
@Order(-1)
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 所有异常信息
     *
     * @param exception
     * @return
     * @throws Exception
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseResult<String> exceptionHandler(Exception exception) {
        log.error("exceptionHandle {}\n{}", exception.getMessage(), ExceptionUtil.stacktraceToString(exception));
        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR, exception.getMessage());
    }


}
