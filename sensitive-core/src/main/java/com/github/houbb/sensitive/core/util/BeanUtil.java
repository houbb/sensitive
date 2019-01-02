package com.github.houbb.sensitive.core.util;

import java.lang.reflect.Field;
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
     * 将 source 中的值赋给 target，
     * 条件是属性的名字+类型相同
     * @param source 原始对象
     * @param target 目标赋值对象
     */
    public static void copyProperties(final Object source, Object target) {
        try {
            List<Field> sourceFieldList = ReflectionUtil.getAllFieldList(source);
            Map<String, Field> targetFieldMap = ReflectionUtil.getAllFieldMap(target);

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
