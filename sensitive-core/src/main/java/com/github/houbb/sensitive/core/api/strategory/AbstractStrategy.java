package com.github.houbb.sensitive.core.api.strategory;

import com.github.houbb.sensitive.api.IContext;
import com.github.houbb.sensitive.api.IStrategy;

/**
 * 抽象策略
 *
 * @author binbin.hou
 * @since 1.0.0
 */
public abstract class AbstractStrategy implements IStrategy {

    protected abstract Object doDes(Object original, IContext context);

    @Override
    public Object des(Object original, IContext context) {
        if(original == null) {
            return original;
        }

        return doDes(original, context);
    }

}
