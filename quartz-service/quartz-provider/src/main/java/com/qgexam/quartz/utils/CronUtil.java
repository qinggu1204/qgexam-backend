package com.qgexam.quartz.utils;

import java.time.LocalDateTime;

/**
 * @author yzw
 * @date 2023年01月09日 0:06
 */
public class CronUtil {

    // 私有构造方法
    private CronUtil() {
    }
    /**
     * 将传入的LocalDateTime转换为cron表达式
     * @author yzw
     * @date 2023/1/9 0:09
     * @param localDateTime
     * @return String
     */

    public static String localDateTimeToCron(LocalDateTime localDateTime) {
        // 获取jobStartTime中的秒数
        int second = localDateTime.getSecond();
        // 获取jobStartTime中的分钟数
        int minute = localDateTime.getMinute();
        // 获取jobStartTime中的小时数
        int hour = localDateTime.getHour();
        // 获取jobStartTime中的天数
        int day = localDateTime.getDayOfMonth();
        // 获取jobStartTime中的月数
        int month = localDateTime.getMonthValue();
        // 获取jobStartTime中的年数
        int year = localDateTime.getYear();
        return String.format("%d %d %d %d %d ? %d", second, minute, hour, day, month, year);
    }
}
