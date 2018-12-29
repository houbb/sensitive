package com.github.houbb.sensitive.api.impl;

import com.github.houbb.sensitive.api.ICondition;
import com.github.houbb.sensitive.api.IContext;

/**
 * 一致返回真的条件
 * @author binbin.hou
 * @date 2018/12/29
 */
public class ConditionAlwaysTrue implements ICondition {
    @Override
    public boolean valid(IContext context) {
        return true;
    }
}
