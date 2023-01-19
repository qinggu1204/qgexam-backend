package com.qgexam.common.core.utils;


import cn.hutool.core.date.DateUtil;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author peter guo
 * @Description 日期时间转Cron表达式
 * @date 2023/01/04 20:01:42
 */
public class DateTimeToCronUtils {

    /**
     * 每年format格式
     */
    public static final String YEAR = "ss mm HH dd MM ? yyyy";

    /**
     * 每周format格式
     */
    public static final String MONDAY = "ss mm HH ? * 1";
    public static final String TUESDAY = "ss mm HH ? * 2";
    public static final String WEDNESDAY = "ss mm HH ? * 3";
    public static final String THURSDAY = "ss mm HH ? * 4";
    public static final String FRIDAY = "ss mm HH ? * 5";
    public static final String SATURDAY = "ss mm HH ? * 6";
    public static final String SUNDAY = "ss mm HH ? * 7";

    /**
     * 每天format格式
     */
    public static final String EVERYDAY = "ss mm HH * * ?";

    /**
     * 间隔-每天format格式
     */
    public static final String INTERVAL_DAY = "0 0 0 1/param * ? ";

    /**
     * 间隔-每小时format格式
     */
    public static final String INTERVAL_HOUR = "0 0 0/param * * ?";

    /**
     * 间隔-每分钟format格式
     */
    public static final String INTERVAL_MINUTE = "0 0/param * * * ? ";

    /**
     * LocalDateTime格式化为String
     *
     * @param date       LocalDateTime
     * @param dateFormat format格式
     * @return String
     * @author longwei
     */
    public static String formatDateByPattern(LocalDateTime date, String dateFormat) {
        return DateUtil.format(date, dateFormat);
    }

    /**
     * date格式化为String
     *
     * @param date       date
     * @param dateFormat format格式
     * @return String
     * @author longwei
     */
    public static String formatDateByPattern(Date date, String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        String formatTimeStr = null;
        if (date != null) {
            formatTimeStr = sdf.format(date);
        }
        return formatTimeStr;
    }

    /**
     * 时间转换Cron表达式
     *
     * @param date       date
     * @param dateFormat format格式
     * @return Cron表达式
     * @author longwei
     */
    public static String getCron(Date date, String dateFormat) {
        return formatDateByPattern(date, dateFormat);
    }

    /**
     * 时间转换Cron表达式
     *
     * @param date       date
     * @param dateFormat format格式
     * @return Cron表达式
     * @author longwei
     */
    public static String getCron(LocalDateTime date, String dateFormat) {
        return formatDateByPattern(date, dateFormat);
    }

    /**
     * 间隔天转换Cron表达式
     *
     * @param param 天
     * @return Cron表达式
     * @author longwei
     */
    public static String getIntervalDayCron(String param) {
        return INTERVAL_DAY.replace("param", param);
    }

    /**
     * 间隔小时转换Cron表达式
     *
     * @param param 小时
     * @return Cron表达式
     * @author longwei
     */
    public static String getIntervalHourCron(String param) {
        return INTERVAL_HOUR.replace("param", param);
    }

    /**
     * 间隔分钟转换Cron表达式
     *
     * @param param 分钟
     * @return Cron表达式
     * @author longwei
     */
    public static String getIntervalMinuteCron(String param) {
        return INTERVAL_MINUTE.replace("param", param);
    }

}
