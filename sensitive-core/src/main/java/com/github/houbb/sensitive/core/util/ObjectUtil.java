package com.github.houbb.sensitive.core.util;

/**
 * 对象工具类
 * @author binbin.hou
 * date 2019/1/2
 */
public final class ObjectUtil {

    private ObjectUtil(){}

    /**
     * 对象是否为空
     * @param object 入参
     * @return 是否为 null
     */
    public static boolean isNull(final Object object) {
        return null == object;
    }

}
