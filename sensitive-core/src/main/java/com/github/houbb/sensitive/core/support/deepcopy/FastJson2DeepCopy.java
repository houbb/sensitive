package com.github.houbb.sensitive.core.support.deepcopy;

import com.alibaba.fastjson2.JSON;
import com.github.houbb.sensitive.api.IDeepCopy;
import com.github.houbb.sensitive.core.support.InnerJsonUtil;

/**
 * 基于 FastJson2 的深度拷贝实现
 * @author binbin.hou
 * @since 1.7.1
 */
public class FastJson2DeepCopy implements IDeepCopy {

    /**
     * 静态内部类-单例
     */
    private static class SingletonHolder {
        private static final FastJson2DeepCopy INSTANCE = new FastJson2DeepCopy();
    }

    /**
     * 获取单例实例
     * @return 实例
     */
    public static FastJson2DeepCopy getInstance() {
        return SingletonHolder.INSTANCE;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T deepCopy(T object) {
        String json = InnerJsonUtil.toJson(object);
        return (T) JSON.parseObject(json, object.getClass());
    }

}