package com.qgexam.common.core.utils;

import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 对象属性拷贝
 */
public class BeanCopyUtils {

    private BeanCopyUtils() {
    }

    public static <V> V copyBean(Object source,Class<V> targetType) {
        //创建目标对象
        V result = null;
        try {
            result = targetType.newInstance();
            //实现属性copy
            BeanUtils.copyProperties(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //返回结果
        return result;
    }
    public static <O,V> List<V> copyBeanList(List<O> source, Class<V> targetType){
        return source.stream()
                .map(o -> copyBean(o, targetType))
                .collect(Collectors.toList());
    }
}

