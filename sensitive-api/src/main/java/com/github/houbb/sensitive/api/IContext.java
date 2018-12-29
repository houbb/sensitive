package com.github.houbb.sensitive.api;

import java.lang.reflect.Field;
import java.util.List;

/**
 * 脱敏的上下文
 * @author binbin.hou
 * @date 2018/12/29
 */
public interface IContext {

    /**
     * 获取所有的上下文
     * @return field 列表
     */
    List<Field> getAllFieldList();

}
