package com.github.houbb.sensitive.api;

import com.github.houbb.hash.api.IHash;

/**
 * 脱敏配置接口
 * @author binbin.hou
 * @since 0.0.9
 */
public interface ISensitiveConfig {

    /**
     * 深度拷贝
     * @return 深度拷贝
     * @since 0.0.9
     */
    IDeepCopy deepCopy();

    /**
     * 哈希策略
     * @return 结果
     * @since 1.1.0
     */
    IHash hash();

    /**
     * 是否显示哈希值
     * @return 是否显示哈希值，默认 true
     * @since 1.8.0
     */
    default boolean showHash() {
        return true;
    }

}

