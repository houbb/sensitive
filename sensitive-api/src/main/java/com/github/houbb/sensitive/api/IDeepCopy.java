package com.github.houbb.sensitive.api;

/**
 * 深度拷贝接口
 * @author binbin.hou
 * @since 0.0.9
 */
public interface IDeepCopy {

    /**
     * 深度拷贝
     * @param object 原始对象
     * @return 结果
     * @since 0.0.9
     * @param <T> 泛型
     */
    <T> T deepCopy(T object);

}
