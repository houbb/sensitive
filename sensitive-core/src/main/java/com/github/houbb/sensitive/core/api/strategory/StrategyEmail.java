package com.github.houbb.sensitive.core.api.strategory;

import com.github.houbb.sensitive.api.IContext;
import com.github.houbb.sensitive.api.IStrategy;
import com.github.houbb.sensitive.core.util.ObjectUtil;
import com.github.houbb.sensitive.core.util.StrUtil;

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
        if(ObjectUtil.isNull(original)) {
            return null;
        }

        final String emailStr = original.toString();
        final int prefixLength = 3;

        final int atIndex = emailStr.indexOf(StrUtil.AT);
        String middle = "****";

        if(atIndex > 0) {
            int middleLength = atIndex - prefixLength;
            middle = StrUtil.repeat(StrUtil.STAR, middleLength);
        }
        return StrUtil.buildString(original, middle, prefixLength);
    }

}
