package com.github.houbb.sensitive.core.api.strategory;

import com.github.houbb.sensitive.api.IContext;
import com.github.houbb.sensitive.api.IStrategy;
import com.github.houbb.sensitive.core.util.ObjectUtil;
import com.github.houbb.sensitive.core.util.StrUtil;

/**
 * 中文名称脱敏策略：
 * 0. 少于等于1个字 直接返回
 * 1. 两个字 隐藏姓
 * 2. 三个及其以上 只保留第一个和最后一个 其他用星号代替
 *
 * @author binbin.hou
 * @date 2019/1/2
 */
public class StrategyChineseName implements IStrategy {

    @Override
    public Object des(Object original, IContext context) {
        if(ObjectUtil.isNull(original)) {
            return original;
        }

        final String originalStr = original.toString();
        final int nameLength = originalStr.length();
        if(1 == nameLength) {
            return originalStr;
        }

        if(2 == nameLength) {
            return StrUtil.STAR + originalStr.charAt(1);
        }

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(originalStr.charAt(0));
        for(int i = 0; i < nameLength-2; i++) {
            stringBuffer.append(StrUtil.STAR);
        }
        stringBuffer.append(originalStr.charAt(nameLength -1));
        return stringBuffer.toString();
    }

}
