package com.github.houbb.sensitive.core.api.strategory;

import com.github.houbb.sensitive.api.IContext;

/**
 * 密码的脱敏策略：
 * 1. 直接返回 null
 * @author binbin.hou
 * date 2018/12/29
 */
public class StrategyPassword extends AbstractStrategy {

    @Override
    protected Object doDes(Object original, IContext context) {
        return null;
    }

}
