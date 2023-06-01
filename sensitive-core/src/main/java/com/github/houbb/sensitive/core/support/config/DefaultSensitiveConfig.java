package com.github.houbb.sensitive.core.support.config;

import com.github.houbb.deep.copy.api.IDeepCopy;
import com.github.houbb.hash.api.IHash;
import com.github.houbb.sensitive.api.ISensitiveConfig;

/**
 * 默认脱敏配置实现
 * @author binbin.hou
 * @since 0.0.9
 */
public class DefaultSensitiveConfig implements ISensitiveConfig {

    /**
     * 深度拷贝实现
     * @since 0.0.9
     */
    private IDeepCopy deepCopy;

    /**
     * 哈希策略
     */
    private IHash hash;

    /**
     * 新建对象实例
     * @since 0.0.9
     * @return 实例
     */
    public static DefaultSensitiveConfig newInstance() {
        return new DefaultSensitiveConfig();
    }

    public DefaultSensitiveConfig deepCopy(IDeepCopy deepCopy) {
        this.deepCopy = deepCopy;
        return this;
    }

    @Override
    public IDeepCopy deepCopy() {
        return deepCopy;
    }

    @Override
    public IHash hash() {
        return hash;
    }

    public DefaultSensitiveConfig hash(IHash hash) {
        this.hash = hash;
        return this;
    }

}
