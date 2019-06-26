package com.github.houbb.sensitive.core.api.strategory;

import com.github.houbb.heaven.util.lang.ObjectUtil;
import com.github.houbb.sensitive.api.IContext;
import com.github.houbb.sensitive.api.IStrategy;
import com.github.houbb.sensitive.core.util.strategy.SensitiveStrategyUtil;

/**
 * 邮箱脱敏策略
 * 脱敏规则：
 * 保留前三位，中间隐藏4位。其他正常显示
 * @author binbin.hou
 * date 2019/1/2
 */
public class StrategyEmail implements IStrategy {

    @Override
    public Object des(Object original, IContext context) {
        return SensitiveStrategyUtil.email(ObjectUtil.objectToString(original));
    }

}
