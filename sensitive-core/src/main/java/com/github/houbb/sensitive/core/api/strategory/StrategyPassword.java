package com.github.houbb.sensitive.core.api.strategory;

import com.github.houbb.sensitive.api.IContext;
import com.github.houbb.sensitive.api.IStrategy;

/**
 * 密码的脱敏策略：直接显示为空
 * @author binbin.hou
 * @date 2018/12/29
 */
public class StrategyPassword implements IStrategy {

    @Override
    public Object des(Object original, IContext context) {
        return null;
    }

}
