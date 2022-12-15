package com.qgexam.common.core.context;

import java.util.HashMap;
import java.util.Map;

/**
 * @project qgexam
 * @description 线程帮助类， 在threadLocal中存储当前用户信息
 * @author yzw
 * @date 2022/12/16 00:32:18
 * @version 1.0
 */
public class BaseContextHandler {

    public static ThreadLocal<Map<String, Object>> threadLocal = new ThreadLocal<>();
    public static final String CONTEXT_KEY_USER_ID = "currentUserId";
    public static final String CONTEXT_KEY_USERNAME = "currentUserName";

    public static void set(String key, Object value) {
        Map<String, Object> map = threadLocal.get();
        if (map == null) {
            map = new HashMap<String, Object>();
            threadLocal.set(map);
        }
        map.put(key, value);
    }

    public static Object get(String key) {
        Map<String, Object> map = threadLocal.get();
        if (map == null) {
            map = new HashMap<String, Object>();
            threadLocal.set(map);
        }
        return map.get(key);
    }

    public static void setUserID(String userID) {
        set(CONTEXT_KEY_USER_ID, userID);
    }

    public static String getUserID() {
        Object value = get(CONTEXT_KEY_USER_ID);
        return returnObjectValue(value);
    }

    public static void setUsername(String username) {
        set(CONTEXT_KEY_USERNAME, username);
    }

    public static String getUsername() {
        Object value = get(CONTEXT_KEY_USERNAME);
        return returnObjectValue(value);
    }

    private static String returnObjectValue(Object value) {
        return value == null ? null : value.toString();
    }

    public static void remove() {
        threadLocal.remove();
    }

}
