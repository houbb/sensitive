package com.github.houbb.sensitive.core.api.strategory;

import com.github.houbb.heaven.util.lang.ObjectUtil;
import com.github.houbb.sensitive.api.IContext;
import com.github.houbb.sensitive.api.IStrategy;
import com.github.houbb.sensitive.core.util.strategy.SensitiveStrategyUtil;

/**
 * 身份证号脱敏
 * @author binbin.hou
 * @since 0.0.15
 */
public class StrategyIdNo implements IStrategy {

    @Override
    public Object des(Object original, IContext context) {
        return SensitiveStrategyUtil.idNo(ObjectUtil.objectToString(original));
    }

}
