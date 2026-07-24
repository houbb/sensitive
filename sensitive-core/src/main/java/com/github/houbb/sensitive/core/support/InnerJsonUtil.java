package com.github.houbb.sensitive.core.support;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.filter.ContextValueFilter;

/**
 * 内部 JSON 工具类
 * @since 1.7.1
 */
public final class InnerJsonUtil {

    private InnerJsonUtil() {
    }

    /**
     * 对象转 JSON 字符串（不使用过滤器）
     * @param object 对象
     * @return JSON 字符串
     */
    public static String toJson(Object object) {
        return JSON.toJSONString(object, JSONWriter.Feature.NotWriteDefaultValue);
    }

    /**
     * 对象转 JSON 字符串（使用过滤器）
     * @param object 对象
     * @param filter 值过滤器
     * @return JSON 字符串
     */
    public static String toJson(Object object, ContextValueFilter filter) {
        return JSON.toJSONString(object, filter, JSONWriter.Feature.NotWriteDefaultValue);
    }

}