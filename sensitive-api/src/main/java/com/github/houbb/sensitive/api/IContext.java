package com.github.houbb.sensitive.api;

import java.lang.reflect.Field;
import java.util.List;

/**
 * 脱敏的上下文
 * @author binbin.hou
 * date 2018/12/29
 * @since 0.0.1
 */
public interface IContext {

    /**
     * 获取所有的上下文
     * @return field 列表
     */
    List<Field> getAllFieldList();

    /**
     * 获得当前字段信息
     * @return 字段信息
     */
    Field getCurrentField();

    /**
     * 获取当前对象
     * @return 当前对象
     */
    Object getCurrentObject();

}
