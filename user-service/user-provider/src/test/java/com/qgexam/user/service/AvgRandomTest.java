package com.qgexam.user.service;

import com.qgexam.common.core.constants.ExamConstants;

import java.util.Random;

/**
 * @author peter guo
 * @description 给出平均值生成n个随机数
 * @date 2022/12/24 16:00:07
 */
public class AvgRandomTest {
    public static void main(String[] args) {
        /*double x;
        int avg = 5;
        int min = ExamConstants.DIFFICULTY_LEVEL_MINIMUM;
        int max = ExamConstants.DIFFICULTY_LEVEL_MAXIMUM;
        int num = 10;
        x = (double) (max - avg) / (max - min);
        System.out.println(x);
        Random random = new Random();
        double rad;
        int money;
        double total = 0;
        int i = num;
        while (i > 0) {
            rad = random.nextDouble();
            if (rad < x) {
                money = 1 + random.nextInt(avg);
            } else {
                money = avg + 1 + random.nextInt(max - avg);
            }
            System.out.println(money);
            total = total + money;
            i--;
        }
        System.out.println(total);
        System.out.println("平均值：" + (total / 10));*/

        double f = Math.round(0.29166666666666666666666666666667 * 100) / 100.0;
        System.out.println(f);
    }
}
