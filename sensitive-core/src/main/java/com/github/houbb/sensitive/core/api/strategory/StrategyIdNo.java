package com.github.houbb.sensitive.core.api.strategory;

import com.github.houbb.sensitive.api.IContext;

/**
 * 身份证号脱敏
 * @author binbin.hou
 * @since 0.0.15
 */
public class StrategyIdNo extends AbstractStringStrategy {

    @Override
    protected int getBeforeMaskLen(Object original, IContext context, char[] chars) {
        return 1;
    }

    @Override
    protected int getAfterMaskLen(Object original, IContext context, char[] chars) {
        return 1;
    }

}
