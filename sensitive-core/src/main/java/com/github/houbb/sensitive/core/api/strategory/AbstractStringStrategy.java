package com.github.houbb.sensitive.core.api.strategory;

import com.github.houbb.heaven.util.lang.ObjectUtil;
import com.github.houbb.sensitive.api.IContext;
import com.github.houbb.sensitive.core.constant.SensitiveConst;

/**
 * 抽象字符串策略
 *
 * @author binbin.hou
 * @since 1.0.0
 */
public abstract class AbstractStringStrategy extends AbstractStrategy {

    /**
     * 获取掩码之前的长度
     * @param original 原始
     * @param context 上下文
     * @param chars 字符串
     * @return 结果
     */
    protected abstract int getBeforeMaskLen(Object original, IContext context, char[] chars);

    /**
     * 获取掩码之后的长度
     * @param original 原始
     * @param context 上下文
     * @param chars 字符串
     * @return 结果
     */
    protected abstract int getAfterMaskLen(Object original, IContext context, char[] chars);

    @Override
    protected Object doDes(Object original, IContext context) {
        String strValue = ObjectUtil.objectToString(original);
        char[] chars = strValue.toCharArray();

        try {
            // 缓存，可以考虑优化为 ThreadLocal
            StringBuilder stringBuilder = StrategyBufferThreadLocal.getBuffer();

            int beforeMaskLen = getBeforeMaskLen(original, context, chars);
            int afterMaskLen = getAfterMaskLen(original, context, chars);

            //范围纠正
            int maxLen = chars.length;
            beforeMaskLen = Math.min(beforeMaskLen, maxLen);
            afterMaskLen = Math.min(afterMaskLen, maxLen);

            if(beforeMaskLen > 0) {
                stringBuilder.append(chars, 0, beforeMaskLen);
            }
            // 中间使用掩码
            for(int i = beforeMaskLen; i < chars.length - afterMaskLen; i++) {
                stringBuilder.append(SensitiveConst.STAR);
            }
            if(afterMaskLen > 0) {
                stringBuilder.append(chars, chars.length - afterMaskLen, afterMaskLen);
            }

            return stringBuilder.toString();
        } finally {
            StrategyBufferThreadLocal.clearBuffer();
        }
    }

}
