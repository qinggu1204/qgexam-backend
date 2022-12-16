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
    public static final String CONTEXT_KEY_USER_NAME = "currentUserName";
    public static final String CONTEXT_KEY_STUDENT_ID = "currentStudentId";
    public static final String CONTEXT_KEY_TEACHER_ID = "currentTeacherId";


    public static void set(String key, Object value) {
        Map<String, Object> map = threadLocal.get();
        if (map == null) {
            map = new HashMap<String, Object>();
        }
        map.put(key, value);
        threadLocal.set(map);
    }

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

    public static void setUserName(String userName) {
        set(CONTEXT_KEY_USER_NAME, userName);
    }
    public static String getUserName() {
        Object value = get(CONTEXT_KEY_USER_NAME);
        return returnObjectValue(value);
    }
    public static void setStudentID(Integer studentID) {
        set(CONTEXT_KEY_STUDENT_ID, studentID);
    }

    public static Integer getStudentID() {
        Object value = get(CONTEXT_KEY_STUDENT_ID);
        return returnObjectValue(value);
    }

    public static void setTeacherID(Integer teacherID) {
        set(CONTEXT_KEY_TEACHER_ID, teacherID);
    }

    public static Integer getTeacherID() {
        Object value = get(CONTEXT_KEY_TEACHER_ID);
        return returnObjectValue(value);
    }


    /**
     * 传入的Object类型的数据，判断是否为空，不为空则转换为T类型的数据
     * @param value
     * @return
     */
    private static <T> T returnObjectValue(Object value) {
        return value == null ? null : (T) value;
    }


    public static void remove() {
        threadLocal.remove();
    }

}
