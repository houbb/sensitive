package com.github.houbb.sensitive.core.api.strategory;

import com.github.houbb.sensitive.annotation.strategy.SensitiveStrategyMaskRange;
import com.github.houbb.sensitive.api.IContext;

import java.lang.reflect.Field;

/**
 * 范围字符串策略
 *
 * @author binbin.hou
 * @since 1.0.0
 */
public class StrategyMaskRange extends AbstractStringStrategy {

    @Override
    protected int getBeforeMaskLen(Object original, IContext context, char[] chars) {
        Field field = context.getCurrentField();
        SensitiveStrategyMaskRange range = field.getAnnotation(SensitiveStrategyMaskRange.class);
        return range.beforeLen();
    }

    @Override
    protected int getAfterMaskLen(Object original, IContext context, char[] chars) {
        Field field = context.getCurrentField();
        SensitiveStrategyMaskRange range = field.getAnnotation(SensitiveStrategyMaskRange.class);
        return range.afterLen();
    }

}
