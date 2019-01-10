package com.github.houbb.sensitive.core.util;

import java.util.*;

/**
 * 数组工具类
 *
 * @author binbin.hou
 * date 2019/1/10
 * @since 0.0.2
 */
public final class ArrayUtil {

    private ArrayUtil() {
    }

    /**
     * 数组转换为列表
     *
     * @param objects 数组
     * @return 列表
     */
    public static List<Object> toList(final Object[] objects) {
        if (ArrayUtil.isEmpty(objects)) {
            return Collections.emptyList();
        }

        List<Object> objectList = new ArrayList<>(objects.length);
        objectList.addAll(Arrays.asList(objects));
        return objectList;
    }

    /**
     * 转换为数组
     * @param objectList 集合
     * @return 对象数组
     */
    public static Object[] toArray(final List<?> objectList) {
        if (CollectionUtil.isEmpty(objectList)) {
            return new Object[0];
        }

        Object[] objects = new Object[objectList.size()];
        for (int i = 0; i < objects.length; i++) {
            objects[i] = objectList.get(i);
        }
        return objects;
    }

    /**
     * 数组是否为空
     *
     * @param objects 数组对象
     * @return 是否为空
     */
    public static boolean isEmpty(Object[] objects) {
        if (null == objects
                || objects.length <= 0) {
            return true;
        }

        return false;
    }

    /**
     * 数组是否不为空
     *
     * @param objects 数组对象
     * @return 是否为空
     */
    public static boolean isNotEmpty(Object[] objects) {
        return !isEmpty(objects);
    }

}
