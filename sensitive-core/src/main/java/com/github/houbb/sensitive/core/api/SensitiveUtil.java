package com.github.houbb.sensitive.core.api;

import com.github.houbb.heaven.support.instance.impl.Instances;

import java.util.Collection;

/**
 * 脱敏工具类
 * @author binbin.hou
 * date 2018/12/29
 * @since 0.0.1
 */
public final class SensitiveUtil {

    private SensitiveUtil(){}

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
        return (T) Instances.singletion(SensitiveService.class)
                .desCopy(object);
    }

    /**
     * 脱敏集合内对象
     *
     * 循环调用desCopy实现
     * 可以使用 {@link ThreadLocal} 简单优化。
     * @param srcCollection 原始对象集合
     * @param <T> 泛型
     * @return 脱敏后的对象集合
     * @since 0.0.4 以前用的是单例。建议使用 spring 等容器管理 ISensitive 实现。
     */
    public static <T> Collection<T> desCopyCollection(Collection<T> srcCollection){
        return Instances.singletion(SensitiveService.class)
                .desCopyCollection(srcCollection);
    }

    /**
     * 返回脱敏后的对象 json
     * null 对象，返回字符串 "null"
     * @param object 对象
     * @return 结果 json
     * @since 0.0.6
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static String desJson(Object object) {
        return Instances.singletion(SensitiveService.class)
                .desJson(object);
    }

}
