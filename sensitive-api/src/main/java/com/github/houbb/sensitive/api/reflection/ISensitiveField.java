package com.github.houbb.sensitive.api.reflection;

/**
 * 反射的 field 信息
 * 作用：对于反射的 field 信息进行简单封装，便于使用。
 * @author binbin.hou
 * date 2019/1/9
 * @since 0.0.2
 * @see com.github.houbb.sensitive.api.IContext 执行上下文
 */
public interface ISensitiveField {

    /**
     * 获取字段名称
     * @return 字段名称
     */
    String getName();

    /**
     * 获取字段值
     * @return 值
     */
    Object getValue();

}
