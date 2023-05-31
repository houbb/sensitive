package com.github.houbb.sensitive.core.api.strategory;

import com.github.houbb.sensitive.api.IContext;

/**
 * 中文名称脱敏策略：
 * 0. 少于等于1个字 直接返回
 * 1. 两个字 隐藏姓
 * 2. 三个及其以上 只保留第一个和最后一个 其他用星号代替
 *
 * @author binbin.hou
 * date 2019/1/2
 */
public class StrategyChineseName extends AbstractStringStrategy {

    @Override
    protected int getBeforeMaskLen(Object original, IContext context, char[] chars) {
        return 1;
    }

    @Override
    protected int getAfterMaskLen(Object original, IContext context, char[] chars) {
        return 0;
    }

}
