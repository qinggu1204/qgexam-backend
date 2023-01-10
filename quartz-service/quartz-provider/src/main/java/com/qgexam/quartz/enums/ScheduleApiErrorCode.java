package com.qgexam.quartz.enums;


public enum ScheduleApiErrorCode  {

    CRON_EXPRESSION_ERROR(3001, "cron表达式不正确"),

    SCHEDULER_EXPRESSION_ERROR(3002, "调度异常");

    final int code;
    final String msg;

    private ScheduleApiErrorCode(int code, String errorMessage) {
        this.code = code;
        this.msg = errorMessage;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}