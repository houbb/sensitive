package com.github.houbb.sensitive.api;

/**
 * 执行上下文接口
 * @author binbin.hou
 * date 2018/12/29
 * @since 0.0.1
 */
public interface ICondition {

    /**
     * 是否执行脱敏
     * @param context 执行上下文
     * @return 结果：是否执行
     */
    boolean valid(IContext context);

}
