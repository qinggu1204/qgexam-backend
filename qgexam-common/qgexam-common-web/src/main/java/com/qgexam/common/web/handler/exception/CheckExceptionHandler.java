package com.qgexam.common.web.handler.exception;



import com.qgexam.common.core.api.AppHttpCodeEnum;
import com.qgexam.common.core.api.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author：dcy
 * @Description: 校验参数异常处理
 * @Date: 2021/6/2 8:38
 */
@RestControllerAdvice
@Order(-15)
@Slf4j
public class CheckExceptionHandler {

    /**
     * javaBean校验(form-data形式)
     * @param bindException
     * @return
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(BindException.class)
    public ResponseResult<String> bindExceptionExceptionHandler(BindException bindException) {
        log.warn("bindException [{}]", bindException.toString());
        if (bindException.hasErrors()) {
            for (ObjectError error : bindException.getAllErrors()) {
                return ResponseResult.errorResult(AppHttpCodeEnum.CHECK_ERROR, error.getDefaultMessage());
            }
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.CHECK_ERROR, bindException.getMessage());
    }

    /**
     * 参数未填写 @RequestParam
     * @param exception
     * @return
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseResult<String> missingServletRequestParameterExceptionHandler(MissingServletRequestParameterException exception) {
        log.warn("missingServletRequestParameterException [{}]", exception.toString());
        return ResponseResult.errorResult(AppHttpCodeEnum.CHECK_ERROR, exception.getMessage());
    }

    /**
     * 基本数据类型，验证错误
     * @param exception
     * @return
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseResult<String> constraintViolationException(ConstraintViolationException exception) {
        log.warn("constraintViolationException [{}]", exception.toString());
        List<String> messageList = exception.getConstraintViolations()
                .stream()
                .map(constraintViolation -> constraintViolation.getMessage())
                .collect(Collectors.toList());
        return ResponseResult.errorResult(AppHttpCodeEnum.CHECK_ERROR, messageList.get(0));
    }

    /**
     * javaBean参数校验(json形式，即@RequestBody)
     * @param exception
     * @return
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseResult<String> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException exception) {
        log.warn("methodArgumentNotValidException [{}]", exception.toString());
        List<String> defaultMessage = exception.getBindingResult().getAllErrors()
                .stream()
                .map(objectError -> objectError.getDefaultMessage())
                .collect(Collectors.toList());

        return ResponseResult.errorResult(AppHttpCodeEnum.CHECK_ERROR, defaultMessage.get(0));
    }

}
