package com.github.houbb.sensitive.core.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * bean 工具类
 *
 * @author binbin.hou
 * date 2018/12/29
 */
public final class BeanUtil {

    private BeanUtil(){}

    /**
     * 无需进行复制的特殊类型数组
     */
    private static Class[] needlessCloneClasses = new Class[]{String.class, Boolean.class, Character.class, Byte.class, Short.class,
            Integer.class, Long.class, Float.class, Double.class, Void.class, Object.class, Class.class
    };

    /**
     * 判断该类型对象是否无需复制
     *
     * @param c 指定类型
     * @return 如果不需要复制则返回真，否则返回假
     */
    private static boolean isNeedlessClone(Class c) {
        //基本类型
        if (c.isPrimitive()) {
            return true;
        }
        //是否在无需复制类型数组里
        for (Class tmp : needlessCloneClasses) {
            if (c.equals(tmp)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 将 source 中的值赋给 target，
     * 条件是属性的名字+类型相同
     *
     * TODO: 对于对象的拷贝，不应该直接设置。  需要进行深度复制。
     * 1. 不需要深拷贝的常见类型
     * 2. Iterable 对象的深度拷贝
     * 3. Map 对象的深度拷贝
     * 3. Array 数组的深度拷贝
     * 4. 对象本身的深度拷贝，递归调用
     *
     * @param source 原始对象
     * @param target 目标赋值对象
     */
    public static void copyProperties(final Object source, Object target) {
        try {
            List<Field> sourceFieldList = ClassUtil.getAllFieldList(source);
            Map<String, Field> targetFieldMap = ClassUtil.getAllFieldMap(target);

            for (Field sourceField : sourceFieldList) {
                final String sourceFieldName = sourceField.getName();
                Field targetField = targetFieldMap.get(sourceFieldName);
                if(targetField != null
                    && targetField.getType().equals(sourceField.getType())) {
                    final Object sourceFieldValue = sourceField.get(source);
                    final Object copyFieldValue = copyValue(sourceFieldValue);
                    targetField.set(target, copyFieldValue);
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static Object copyValue(final Object sourceFieldValue) {
        return sourceFieldValue;
    }

    /**
     * 判断是否为Bean对象<br>
     * 判定方法是是否存在只有一个参数的setXXX方法
     *
     * @param clazz 待测试类
     * @return 是否为Bean对象
     */
    public static boolean isBean(Class<?> clazz) {
        if (ClassUtil.isNormalClass(clazz)) {
            final Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                if (method.getParameterTypes().length == 1 && method.getName().startsWith("set")) {
                    // 检测包含标准的setXXX方法即视为标准的JavaBean
                    return true;
                }
            }
        }
        return false;
    }

}
