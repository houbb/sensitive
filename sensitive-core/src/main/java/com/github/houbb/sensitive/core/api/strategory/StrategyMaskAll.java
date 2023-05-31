package com.github.houbb.sensitive.core.api.strategory;

import com.github.houbb.sensitive.api.IContext;

/**
 * 全部掩盖字符串策略
 *
 * @author binbin.hou
 * @since 1.0.0
 */
public class StrategyMaskAll extends AbstractStringStrategy {

    @Override
    protected int getBeforeMaskLen(Object original, IContext context, char[] chars) {
        return 0;
    }

    @Override
    protected int getAfterMaskLen(Object original, IContext context, char[] chars) {
        return 0;
    }

}
