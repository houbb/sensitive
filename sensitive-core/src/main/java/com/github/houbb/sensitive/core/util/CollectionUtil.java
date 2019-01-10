package com.github.houbb.sensitive.core.util;

import java.util.Collection;

/**
 * 集合工具类
 * @author binbin.hou
 * date 2019/1/10
 * @since 0.0.2
 */
public final class CollectionUtil {

    private CollectionUtil(){}

    /**
     * 集合是否为空
     *
     * @param collection 集合
     * @return 是否为空
     */
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * 集合是否为非空
     *
     * @param collection 集合
     * @return 是否为非空
     */
    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }





}
