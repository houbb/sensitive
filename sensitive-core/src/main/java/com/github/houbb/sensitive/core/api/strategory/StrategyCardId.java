package com.github.houbb.sensitive.core.api.strategory;

import com.github.houbb.sensitive.api.IContext;
import com.github.houbb.sensitive.api.IStrategy;
import com.github.houbb.sensitive.core.util.StrUtil;

/**
 * 二代身份证号脱敏加密：
 * XXXXXX XXXXXXXX XXXX
 * 脱敏规则：421002**********34
 *
 * 只保留前6位和后2位，其他用*代替。
 * @author binbin.hou
 * date 2019/1/2
 */
public class StrategyCardId implements IStrategy {

    @Override
    public Object des(Object original, IContext context) {
        final int prefixLength = 6;
        final String middle = "**********";
        return StrUtil.buildString(original, middle, prefixLength);
    }

}
