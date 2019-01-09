package com.github.houbb.sensitive.core.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * class 工具类
 * @author binbin.hou
 * date 2018/12/29
 * @since 0.0.2
 */
public final class ClassUtil {

    private ClassUtil(){}

    /**
     * 获取所有对象字段
     * @param object 对象
     * @return 字段列表
     * @since 0.0.1
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
     * @since 0.0.1
     */
    public static List<Field> getAllFieldList(final Class clazz) {
        Set<Field> fieldSet = new HashSet<>() ;
        Class tempClass = clazz;
        while (tempClass != null) {
            fieldSet.addAll(Arrays.asList(tempClass.getDeclaredFields()));
            tempClass = tempClass.getSuperclass();
        }

        for(Field field : fieldSet) {
            field.setAccessible(true);
        }
        return new ArrayList<>(fieldSet);
    }

    /**
     * 获取一个对象的所有字段 map
     * @param object 对象
     * @return map key 是属性的名字，value 是 field
     * @since 0.0.1
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

    /**
     * 是否为抽象类
     *
     * @param clazz 类
     * @return 是否为抽象类
     * @since 0.0.2
     */
    private static boolean isAbstract(Class<?> clazz) {
        return Modifier.isAbstract(clazz.getModifiers());
    }

    /**
     * 是否为标准的类<br>
     * 这个类必须：
     *
     * <pre>
     * 1、非接口
     * 2、非抽象类
     * 3、非Enum枚举
     * 4、非数组
     * 5、非注解
     * 6、非原始类型（int, long等）
     * </pre>
     *
     * @param clazz 类
     * @return 是否为标准类
     * @since 0.0.2
     */
    public static boolean isNormalClass(Class<?> clazz) {
        return null != clazz
                && !clazz.isInterface()
                && !isAbstract(clazz)
                && !clazz.isEnum()
                && !clazz.isArray()
                && !clazz.isAnnotation()
                && !clazz.isSynthetic()
                && !clazz.isPrimitive();
    }

}
