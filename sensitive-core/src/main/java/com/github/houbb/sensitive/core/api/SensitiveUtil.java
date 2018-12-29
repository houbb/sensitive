package com.github.houbb.sensitive.core.api;

import com.github.houbb.sensitive.api.ISensitive;

/**
 * 脱敏工具类
 * @author binbin.hou
 * @date 2018/12/29
 */
public final class SensitiveUtil {

    private static final ISensitive SINGLETON = new SensitiveService();

    /**
     * 脱敏对象
     * @param object 原始对象
     * @return 脱敏后的对象
     */
    public static Object desCopy(Object object) {
        return SINGLETON.desCopy(object);
    }

    /**
     * 脱敏对象的 toString()
     * @param object 原始对象
     * @return 脱敏后对象的 toString();
     */
    public static String desString(Object object) {
        return SINGLETON.desString(object);
    }

}
