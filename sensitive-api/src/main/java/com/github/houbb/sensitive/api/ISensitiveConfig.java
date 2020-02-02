package com.github.houbb.sensitive.api;

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

}

