package com.github.houbb.sensitive.api;

/**
 * 脱敏接口
 * @author binbin.hou
 * date 2018/12/29
 * @param <T> 参数类型
 * @since 0.0.1
 */
public interface ISensitive<T> {

    /**
     * 对象进行脱敏操作
     * 原始对象不变，返回脱敏后的新对象
     * 1. 为什么这么设计？
     * 不能因为脱敏，就导致代码中的对象被改变。否则代码逻辑会出现问题。
     * @param object 原始对象
     * @param config 配置信息
     * @return 脱敏后的新对象
     */
    T desCopy(final T object, final ISensitiveConfig config);

    /**
     * 返回脱敏后的 json
     * 1. 避免 desCopy 造成的对象新建的性能浪费
     * @param object 对象
     * @param config 配置信息
     * @return json
     * @since 0.0.6
     */
    String desJson(final T object, final ISensitiveConfig config);

}
