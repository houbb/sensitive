package com.github.houbb.sensitive.api;

/**
 * 脱敏接口
 * @author binbin.hou
 * @date 2018/12/29
 */
public interface ISensitive {

    /**
     * 对象进行脱敏操作
     * 原始对象不变，返回脱敏后的新对象
     * 1. 为什么这么设计？
     * 不能因为脱敏，就导致代码中的对象被改变。否则代码逻辑会出现问题。
     * @param object 原始对象
     * @return 脱敏后的新对象
     */
    Object desCopy(final Object object);

    /**
     * 脱敏对象的 toString() 便于日志打印
     * object 对象保持不变
     * @param object 原始对象
     * @return 脱敏后的新对象 toString();
     */
    String desString(final Object object);

}
