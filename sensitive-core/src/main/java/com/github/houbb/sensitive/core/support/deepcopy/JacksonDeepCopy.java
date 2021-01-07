package com.github.houbb.sensitive.core.support.deepcopy;

import com.github.houbb.heaven.annotation.ThreadSafe;
import com.github.houbb.sensitive.api.IDeepCopy;
import com.github.houbb.sensitive.core.util.JacksonUtil;

/**
 * fastjson 的深度拷贝
 * @author binbin.hou
 * @since 0.0.9
 */
@ThreadSafe
public class JacksonDeepCopy implements IDeepCopy {

    @Override
    public <T> T deepCopy(T object) {
        return JacksonUtil.deepCopy(object);
    }

}
