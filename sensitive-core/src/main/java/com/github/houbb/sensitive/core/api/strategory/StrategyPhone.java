package com.github.houbb.sensitive.core.api.strategory;

import com.github.houbb.sensitive.api.IContext;

/**
 * 手机号脱敏
 * 脱敏规则：139****6631
 *
 * @author binbin.hou
 * date 2019/1/2
 */
public class StrategyPhone extends AbstractStringStrategy {

    @Override
    protected int getBeforeMaskLen(Object original, IContext context, char[] chars) {
        return 4;
    }

    @Override
    protected int getAfterMaskLen(Object original, IContext context, char[] chars) {
        return 3;
    }

}
