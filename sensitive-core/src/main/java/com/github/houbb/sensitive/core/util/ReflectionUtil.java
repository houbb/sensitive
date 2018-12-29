package com.github.houbb.sensitive.core.util;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 反射工具类
 * @author binbin.hou
 * @date 2018/12/29
 */
public final class ReflectionUtil {

    /**
     * base toString() method;
     * @param thisObj
     * @return
     */
    public static String toString(Object thisObj) {
        Class clazz = thisObj.getClass();

        String entityName = clazz.getSimpleName();
        Field[] fields = clazz.getDeclaredFields();

        StringBuilder stringBuilder = new StringBuilder(String.format("%s{", entityName));

        for(int i = 0; i < fields.length-1; i++) {
            Field field = fields[i];
            stringBuilder.append(buildFieldValue(thisObj, field)).append(",");
        }

        stringBuilder.append(buildFieldValue(thisObj, fields[fields.length-1]));
        stringBuilder.append("}");

        return stringBuilder.toString();
    }

    /**
     * build "field=fieldValue"
     * @param object
     * @param field
     * @return
     */
    private static String buildFieldValue(Object object, Field field) {
        final String format = isType(field, String.class) ? "%s='%s'" : "%s=%s";
        StringBuilder stringBuilder = new StringBuilder();
        Method getMethod = getGetMethod(object.getClass(), field);
        try {
            Object fieldValue = getMethod.invoke(object);
            stringBuilder = new StringBuilder(String.format(format, field.getName(), fieldValue));
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        return stringBuilder.toString();
    }

    /**
     * get the Get() of current field;
     * @param clazz
     * @param field
     * @return
     */
    private static Method getGetMethod(Class clazz, Field field) {
        PropertyDescriptor propertyDescriptor = null;
        try {
            propertyDescriptor = new PropertyDescriptor(field.getName(), clazz);
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }

        return propertyDescriptor.getReadMethod();
    }

    /**
     * adjust just field is the type of
     * @param field
     * @param classType
     * @return
     */
    private static Boolean isType(Field field, Class classType) {
        return field.getType().equals(classType);
    }

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
