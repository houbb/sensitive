package com.github.houbb.sensitive.api;

/**
 * @author binbin.hou
 * date 2018/12/29
 */
public interface ICondition {

    /**
     * 是否执行脱敏
     * @param context 执行上下文
     * @return 结果：是否执行
     */
    boolean valid(IContext context);

}
