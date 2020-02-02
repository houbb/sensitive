package com.github.houbb.sensitive.core.support.deepcopy;

import com.github.houbb.heaven.annotation.ThreadSafe;
import com.github.houbb.sensitive.api.IDeepCopy;
import com.github.houbb.sensitive.core.util.BeanUtil;

/**
 * fastjson 的深度拷贝
 * @author binbin.hou
 * @since 0.0.9
 */
@ThreadSafe
public class FastJsonDeepCopy implements IDeepCopy {

    @Override
    public <T> T deepCopy(T object) {
        return BeanUtil.deepCopy(object);
    }

}
