package com.github.houbb.sensitive.api;

import java.lang.reflect.Field;
import java.util.List;

/**
 * 脱敏的执行上下文
 * @author binbin.hou
 * date 2018/12/29
 * @since 0.0.1
 */
public interface IContext {

    /**
     * 获取所有的字段信息
     * @return field 列表
     * @since 0.0.1
     */
    List<Field> getAllFieldList();

    /**
     * 获得当前字段信息
     * @return 字段信息
     * @since 0.0.1
     */
    Field getCurrentField();

    /**
     * 获取当前字段名称
     * @return 字段名称
     * @since 0.0.4
     */
    String getCurrentFieldName();

    /**
     * 获取当前字段值
     * @return 字段值
     * @since 0.0.4
     */
    Object getCurrentFieldValue();

    /**
     * 获取当前对象
     * @return 当前对象
     * @since 0.0.1
     */
    Object getCurrentObject();

    /**
     * 类信息
     * @return 当前类信息
     * @since 0.0.6
     */
    Class getBeanClass();

    /**
     * 获取当前的明细信息
     * （1）普通字段，默认等于 {@link #getCurrentFieldValue()} 字段值
     * （2）列表/集合/数组，则等于具体的明细信息。
     *
     * 比如 arrays=[1,2,3] 都是字段值
     * 对应的 entry 可能是其中的 {1} 或者元素中的其他一个。
     * @return 明细信息
     * @since 0.0.6
     */
    Object getEntry();

}
