package com.github.houbb.sensitive.core.util;

import com.github.houbb.sensitive.core.exception.SensitiveRuntimeException;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * class 工具类
 *
 * @author binbin.hou
 * date 2018/12/29
 * @since 0.0.2
 */
public final class ClassUtil {

    /**
     * 常见的基础对象类型
     */
    private static final Class[] BASE_TYPE_CLASS = new Class[]{
            String.class, Boolean.class, Character.class, Byte.class, Short.class,
            Integer.class, Long.class, Float.class, Double.class, Void.class, Object.class, Class.class
    };

    private ClassUtil() {
    }

    /**
     * 获取所有对象字段
     *
     * @param object 对象
     * @return 字段列表
     * @since 0.0.1
     */
    public static List<Field> getAllFieldList(final Object object) {
        if (null == object) {
            return Collections.emptyList();
        }
        final Class clazz = object.getClass();
        return getAllFieldList(clazz);
    }

    /**
     * 获取类所有的字段信息
     * ps: 这个方法有个问题 如果子类和父类有相同的字段 会不会重复
     * 1. 还会获取到 serialVersionUID 这个字段。
     *
     * @param clazz 类
     * @return 字段列表
     * @since 0.0.1
     */
    public static List<Field> getAllFieldList(final Class clazz) {
        Set<Field> fieldSet = new HashSet<>();
        Class tempClass = clazz;
        while (tempClass != null) {
            fieldSet.addAll(Arrays.asList(tempClass.getDeclaredFields()));
            tempClass = tempClass.getSuperclass();
        }

        for (Field field : fieldSet) {
            if(Modifier.isFinal(field.getModifiers())
                    && Modifier.isStatic(field.getModifiers())) {
                // 不处理 static final 的字段
                continue;
            }
            field.setAccessible(true);
        }
        return new ArrayList<>(fieldSet);
    }

    /**
     * 获取一个对象的所有字段 map
     *
     * @param object 对象
     * @return map key 是属性的名字，value 是 field
     * @since 0.0.1
     */
    public static Map<String, Field> getAllFieldMap(final Object object) {
        List<Field> fieldList = getAllFieldList(object);
        Map<String, Field> map = new HashMap<>(fieldList.size());

        for (Field field : fieldList) {
            final String fieldName = field.getName();
            map.put(fieldName, field);
        }
        return map;
    }

    /**
     * 是否为 map class 类型
     *
     * @param clazz 对象类型
     * @return 是否为 map class
     */
    public static boolean isMapClass(final Class<?> clazz) {
        return Map.class.equals(clazz);
    }

    /**
     * 是否为 数组 class 类型
     *
     * @param clazz 对象类型
     * @return 是否为 数组 class
     */
    public static boolean isArrayClass(final Class<?> clazz) {
        return clazz.isArray();
    }

    /**
     * 是否为 Collection class 类型
     *
     * @param clazz 对象类型
     * @return 是否为 Collection class
     */
    public static boolean isCollectionClass(final Class<?> clazz) {
        return Collection.class.isAssignableFrom(clazz);
    }

    /**
     * 是否为 Iterable class 类型
     *
     * @param clazz 对象类型
     * @return 是否为 数组 class
     */
    public static boolean isIterableClass(final Class<?> clazz) {
        return Iterable.class.isAssignableFrom(clazz);
    }

    /**
     * 是否为基本类型
     * 1. 8大基本类型
     * 2. 常见的值类型
     *
     * @param clazz 对象类型
     * @return 是否为基本类型
     */
    public static boolean isBaseClass(Class<?> clazz) {
        if (clazz.isPrimitive()) {
            return true;
        }
        for (Class baseClazz : BASE_TYPE_CLASS) {
            if (baseClazz.equals(clazz)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否为抽象类
     *
     * @param clazz 类
     * @return 是否为抽象类
     * @since 0.0.2
     */
    private static boolean isAbstractClass(Class<?> clazz) {
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
    public static boolean isJavaBeanClass(Class<?> clazz) {
        return null != clazz
                && !clazz.isInterface()
                && !isAbstractClass(clazz)
                && !clazz.isEnum()
                && !clazz.isArray()
                && !clazz.isAnnotation()
                && !clazz.isSynthetic()
                && !clazz.isPrimitive();
    }


    /**
     * 获取对象的实例化
     * @param clazz 类
     * @param <T> 泛型
     * @return 实例化对象
     */
    public static <T> T newInstance(final Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new SensitiveRuntimeException(e);
        }
    }
}
