package com.github.houbb.sensitive.api;

/**
 * 深度拷贝接口
 * @author binbin.hou
 * @since 1.7.1
 */
public interface IDeepCopy {

    /**
     * 深度拷贝对象
     * @param object 原始对象
     * @param <T> 泛型
     * @return 拷贝后的对象
     */
    <T> T deepCopy(final T object);

}