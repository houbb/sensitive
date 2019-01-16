package com.github.houbb.sensitive.api.impl;

import com.github.houbb.sensitive.api.IContext;
import com.github.houbb.sensitive.api.IStrategy;
import com.github.houbb.sensitive.api.metadata.ISensitiveStrategy;

/**
 * 用于标识为系统内置的注解实现方式
 * 这个类的实现并不重要，只是为了尽可能降低 annotation 对于实现的依赖。
 * 注意：如果不是系统内置的注解，请勿使用这个标识，否则无法找到对应实现。
 * 在 hibernate-validator 中使用的是数组，然后默认指定 {}，但是缺陷也很明显，
 * 明明是数组，实现却只能是一个。
 * @author binbin.hou
 * date 2019/1/9
 * @since 0.0.2
 */
public class SensitiveStrategyBuiltIn implements ISensitiveStrategy {

    @Override
    public Object des(Object original, IContext context) {
        return null;
    }

}
