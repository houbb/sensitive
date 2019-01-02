package com.github.houbb.sensitive.core.api.strategory;

import com.github.houbb.sensitive.api.IContext;
import com.github.houbb.sensitive.api.IStrategy;
import com.github.houbb.sensitive.core.util.StrUtil;

/**
 * 手机号脱敏
 * 脱敏规则：139****6631
 *
 * @author binbin.hou
 * @date 2019/1/2
 */
public class StrategyPhone implements IStrategy {

    @Override
    public Object des(Object original, IContext context) {
        final int prefixLength = 3;
        final String middle = "****";
        return StrUtil.buildString(original, middle, prefixLength);
    }

}
