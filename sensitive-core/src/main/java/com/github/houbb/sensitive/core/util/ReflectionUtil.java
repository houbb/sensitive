package com.github.houbb.sensitive.core.util;

import java.lang.reflect.Field;
import java.util.*;

/**
 * 反射工具类
 * @author binbin.hou
 * @date 2018/12/29
 */
public final class ReflectionUtil {

    private ReflectionUtil(){}

    /**
     * 获取所有对象字段
     * @param object 对象
     * @return 字段列表
     */
    public static List<Field> getAllFieldList(final Object object) {
        if(null == object) {
            return Collections.emptyList();
        }
        final Class clazz = object.getClass();
        return getAllFieldList(clazz);
    }

    /**
     * 获取类所有的字段信息
     * ps: 这个方法有个问题 如果子类和父类有相同的字段 会不会重复
     * 1. 还会获取到 serialVersionUID 这个字段。
     * @param clazz 类
     * @return 字段列表
     */
    public static List<Field> getAllFieldList(final Class clazz) {
        List<Field> fieldList = new ArrayList<>() ;
        Class tempClass = clazz;
        while (tempClass != null) {
            fieldList.addAll(Arrays.asList(tempClass.getDeclaredFields()));
            tempClass = tempClass.getSuperclass();
        }

        for(Field field : fieldList) {
            field.setAccessible(true);
        }
        return fieldList;
    }

    /**
     * 获取一个对象的所有字段 map
     * @param object 对象
     * @return map key 是属性的名字，value 是 field
     */
    public static Map<String, Field> getAllFieldMap(final Object object) {
        List<Field> fieldList = getAllFieldList(object);
        Map<String, Field> map = new HashMap<>(fieldList.size());

        for(Field field : fieldList) {
            final String fieldName = field.getName();
            map.put(fieldName, field);
        }
        return map;
    }
}
