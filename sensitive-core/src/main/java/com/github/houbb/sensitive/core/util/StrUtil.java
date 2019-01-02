package com.github.houbb.sensitive.core.util;

/**
 * 字符串工具类
 * @author binbin.hou
 * date 2018/12/29
 */
public final class StrUtil {

    private StrUtil(){}

    /**
     * 空字符串
     */
    public static final String EMPTY = "";

    /**
     * 星号
     */
    public static final String STAR = "*";

    /**
     * at 符号
     */
    public static final String AT = "@";

    /**
     * 是否为空
     * @param string 原始字符串
     * @return 是否
     */
    public static boolean isEmpty(final String string) {
        return null == string || EMPTY.equals(string);
    }

    /**
     * 重复多少次
     * @param component 组成信息
     * @param times 重复次数
     * @return 重复多次的字符串结果
     */
    public static String repeat(final String component, final int times) {
        if(StrUtil.isEmpty(component)
            || times <= 0) {
            return StrUtil.EMPTY;
        }

        StringBuffer stringBuffer = new StringBuffer();
        for(int i = 0; i < times; i++) {
            stringBuffer.append(component);
        }

        return stringBuffer.toString();
    }

    /**
     * 构建新的字符串
     * @param original 原始对象
     * @param middle 中间隐藏信息
     * @param prefixLength 前边信息长度
     * @return 构建后的新字符串
     */
    public static String buildString(final Object original,
                                     final String middle,
                                     final int prefixLength) {
        if(ObjectUtil.isNull(original)) {
            return null;
        }

        final String string = original.toString();
        final int stringLength = string.length();

        String prefix = "";
        String suffix = "";

        if(stringLength >= prefixLength) {
            prefix = string.substring(0, prefixLength);
        } else {
            prefix = string.substring(0, stringLength);
        }

        int suffixLength = stringLength - prefix.length() - middle.length();
        if(suffixLength > 0) {
            suffix = string.substring(stringLength -suffixLength);
        }

        return prefix + middle + suffix;
    }

}
