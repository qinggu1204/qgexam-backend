package com.qgexam.common.web.context;

import java.util.HashMap;
import java.util.Map;

/**
 * @project qgexam
 * @description 线程帮助类， 向threadlocal中存放信息
 * @author yzw
 * @date 2022/12/14 11:16:48
 * @version 1.0
 */
public class BaseContextHandler {

    public static ThreadLocal<Map<String, Object>> threadLocal = new ThreadLocal<>();
    public static final String CONTEXT_KEY_USER_ID = "currentUserId";
    public static final String CONTEXT_KEY_USERNAME = "currentLoginName";

    /**
     * 向threadlocal中存放信息
     * @param key
     * @param value
     */
    public static void set(String key, Object value) {
        Map<String, Object> map = threadLocal.get();
        if (map == null) {
            map = new HashMap<String, Object>();
        }
        map.put(key, value);
        threadLocal.set(map);
    }


    /**
     * 从threadlocal中获取信息
     * @param key
     * @return
     */
    public static Object get(String key) {
        Map<String, Object> map = threadLocal.get();
        if (map == null) {
            map = new HashMap<String, Object>();
            threadLocal.set(map);
        }
        return map.get(key);
    }

    public static void setUserID(Integer userID) {
        set(CONTEXT_KEY_USER_ID, userID);
    }

    public static Integer getUserID() {
        Object value = get(CONTEXT_KEY_USER_ID);
        return returnObjectValue(value);
    }

    public static void setUsername(String username) {
        set(CONTEXT_KEY_USERNAME, username);
    }

    public static String getLoginName() {
        Object value = get(CONTEXT_KEY_USERNAME);
        return returnObjectValue(value);
    }


    /**
     * 将传入的Object类型的参数判断是否为空，不为空转换为T类型返回
     */
    private static <T> T returnObjectValue(Object value) {
        return value == null ? null : (T) value;
    }


    public static void remove() {
        threadLocal.remove();
    }

}
