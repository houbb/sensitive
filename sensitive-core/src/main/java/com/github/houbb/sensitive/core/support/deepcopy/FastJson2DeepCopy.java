package com.github.houbb.sensitive.core.support.deepcopy;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import com.github.houbb.sensitive.api.IDeepCopy;
import com.github.houbb.sensitive.core.support.InnerJsonUtil;

/**
 * 基于 FastJson2 的深度拷贝实现
 * @author binbin.hou
 * @since 1.7.1
 */
public class FastJson2DeepCopy implements IDeepCopy {

    /** 是否启用循环引用检测。 */
    private final boolean cycleDetectionEnabled;

    /**
     * 静态内部类-单例
     */
    private static class SingletonHolder {
        private static final FastJson2DeepCopy DEFAULT_INSTANCE = new FastJson2DeepCopy(false);
        private static final FastJson2DeepCopy CYCLE_DETECTION_INSTANCE = new FastJson2DeepCopy(true);
    }

    /**
     * 默认关闭循环引用检测。
     */
    public FastJson2DeepCopy() {
        this(false);
    }

    /**
     * 创建深度拷贝实例。
     *
     * @param cycleDetectionEnabled 是否启用循环引用检测
     * @since 1.9.1
     */
    public FastJson2DeepCopy(boolean cycleDetectionEnabled) {
        this.cycleDetectionEnabled = cycleDetectionEnabled;
    }

    /**
     * 获取单例实例
     * @return 实例
     */
    public static FastJson2DeepCopy getInstance() {
        return SingletonHolder.DEFAULT_INSTANCE;
    }

    /**
     * 根据开关获取不可变的共享实例。
     *
     * @param cycleDetectionEnabled 是否启用循环引用检测
     * @return 实例
     * @since 1.9.1
     */
    public static FastJson2DeepCopy getInstance(boolean cycleDetectionEnabled) {
        return cycleDetectionEnabled
                ? SingletonHolder.CYCLE_DETECTION_INSTANCE
                : SingletonHolder.DEFAULT_INSTANCE;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T deepCopy(T object) {
        if (object == null) {
            return null;
        }

        String json;
        if (cycleDetectionEnabled && ObjectGraphCycleDetector.hasCycle(object)) {
            json = JSON.toJSONString(object,
                    JSONWriter.Feature.NotWriteDefaultValue,
                    JSONWriter.Feature.ReferenceDetection);
        } else {
            // 保持无环对象原有的复制语义，不合并仅仅被多处引用的对象。
            json = InnerJsonUtil.toJson(object);
        }
        return (T) JSON.parseObject(json, object.getClass());
    }

}
