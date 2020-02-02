package com.github.houbb.sensitive.core.support.deepcopy;

import com.github.houbb.heaven.support.instance.impl.Instances;
import com.github.houbb.sensitive.api.IDeepCopy;
import com.github.houbb.sensitive.core.util.BeanUtil;

/**
 * 深度拷贝工具类
 * @author binbin.hou
 * @since 0.0.9
 */
public final class DeepCopies {

    /**
     * 基于 json 的深度拷贝
     * @return 深度拷贝实现
     * @since 0.0.9
     */
    public static IDeepCopy json() {
        return Instances.singleton(FastJsonDeepCopy.class);
    }

}
