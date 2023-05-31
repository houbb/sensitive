package com.github.houbb.sensitive.core.api.strategory;

import com.github.houbb.sensitive.api.IContext;

/**
 * 生日字符串策略
 *
 * @author binbin.hou
 * @since 1.0.0
 */
public class StrategyBirthday extends AbstractStringStrategy {

    @Override
    protected int getBeforeMaskLen(Object original, IContext context, char[] chars) {
        return 2;
    }

    @Override
    protected int getAfterMaskLen(Object original, IContext context, char[] chars) {
        return 1;
    }

}
