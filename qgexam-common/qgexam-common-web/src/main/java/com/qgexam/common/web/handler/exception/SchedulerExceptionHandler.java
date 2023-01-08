package com.qgexam.common.web.handler.exception;


import com.qgexam.common.core.api.ResponseResult;
import com.qgexam.common.quartz.enums.ScheduleApiErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2021/4/8 9:31
 */
@RestControllerAdvice
@Order(-4)
@Slf4j
public class SchedulerExceptionHandler {

    /**
     * 调度异常
     *
     * @param exception
     * @return
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(SchedulerException.class)
    public ResponseResult schedulerExceptionHandler(SchedulerException exception) {
        log.error("schedulerException [{}]", exception.toString());
        exception.printStackTrace();
        return ResponseResult.errorResult(ScheduleApiErrorCode.SCHEDULER_EXPRESSION_ERROR.getCode(),
                ScheduleApiErrorCode.SCHEDULER_EXPRESSION_ERROR.getMsg());
    }


}
