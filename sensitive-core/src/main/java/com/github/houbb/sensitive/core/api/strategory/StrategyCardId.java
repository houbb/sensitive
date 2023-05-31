package com.github.houbb.sensitive.core.api.strategory;

import com.github.houbb.heaven.util.lang.ObjectUtil;
import com.github.houbb.sensitive.api.IContext;
import com.github.houbb.sensitive.api.IStrategy;
import com.github.houbb.sensitive.core.util.strategy.SensitiveStrategyUtil;

/**
 * 银行卡号脱敏：
 * XXXXXX XXXXXXXX XXXX
 * 脱敏规则：123456**********99
 *
 * 只保留前6位和后2位，其他用*代替。
 * @author binbin.hou
 * date 2019/1/2
 */
public class StrategyCardId extends AbstractStringStrategy {

    @Override
    protected int getBeforeMaskLen(Object original, IContext context, char[] chars) {
        return 6;
    }

    @Override
    protected int getAfterMaskLen(Object original, IContext context, char[] chars) {
        return 2;
    }

}
