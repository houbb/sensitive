package com.github.houbb.sensitive.core.util;

/**
 * 字符串工具类
 * @author binbin.hou
 * @date 2018/12/29
 */
public final class StrUtil {

    private StrUtil(){}

    public static boolean isEmpty(final String string) {
        return null == string || "".equals(string);
    }

}
