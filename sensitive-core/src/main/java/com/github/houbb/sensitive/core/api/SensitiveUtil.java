package com.github.houbb.sensitive.core.api;

import com.github.houbb.sensitive.api.ISensitive;

/**
 * 脱敏工具类
 * @author binbin.hou
 * date 2018/12/29
 * @since 0.0.1
 */
public final class SensitiveUtil {

    /**
     * 脱敏对象
     *
     * 每次都创建一个新的对象，避免线程问题
     * 可以使用 {@link ThreadLocal} 简单优化。
     * @param object 原始对象
     * @param <T> 泛型
     * @return 脱敏后的对象
     * @since 0.0.4 以前用的是单例。建议使用 spring 等容器管理 ISensitive 实现。
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T> T desCopy(T object) {
        ISensitive sensitive = new SensitiveService();
        return (T) sensitive.desCopy(object);
    }

}
