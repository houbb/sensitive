package com.github.houbb.sensitive.core.util;

import com.alibaba.fastjson.JSON;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
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
     * 深度复制
     * 1. 为了避免深拷贝要求用户实现 clone 和 序列化的相关接口
     * 2. 为了避免使用 dozer 这种比较重的工具
     * 3. 自己实现暂时工作量比较大
     *
     * 暂时使用 fastJson 作为实现深度拷贝的方式
     * @since 0.0.2
     * @param object 对象
     * @param <T> 泛型
     * @return 深拷贝后的对象
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T> T deepCopy(T object) {
        final Class clazz = object.getClass();
        String jsonString = JSON.toJSONString(object);
        return (T) JSON.parseObject(jsonString, clazz);
    }


    /**
     * 将 source 中的值赋给 target，
     * 条件是属性的名字+类型相同
     *
     * TODO: 对于对象的拷贝，不应该直接设置，需要进行深度复制。
     * 1. 不需要深拷贝的常见类型
     * 2. Iterable 对象的深度拷贝
     * 3. Map 对象的深度拷贝
     * 3. Array 数组的深度拷贝
     * 4. 对象本身的深度拷贝，递归调用
     *
     * @param source 原始对象
     * @param target 目标赋值对象
     * @since 0.0.1
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
                    targetField.set(target, sourceFieldValue);
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
