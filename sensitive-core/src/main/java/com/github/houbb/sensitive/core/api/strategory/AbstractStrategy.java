package com.github.houbb.sensitive.core.api.strategory;

import com.github.houbb.hash.api.IHash;
import com.github.houbb.hash.core.util.HashHelper;
import com.github.houbb.heaven.util.lang.ObjectUtil;
import com.github.houbb.heaven.util.lang.StringUtil;
import com.github.houbb.sensitive.api.IContext;
import com.github.houbb.sensitive.api.ISensitiveConfig;
import com.github.houbb.sensitive.api.IStrategy;

/**
 * 抽象策略
 *
 * @author binbin.hou
 * @since 1.0.0
 */
public abstract class AbstractStrategy implements IStrategy {

    protected abstract Object doDes(Object original, IContext context);

    @Override
    public Object des(Object original, IContext context) {
        // 脱敏的值
        Object resultValue = doDes(original, context);

        // 哈希的值
        if(resultValue != null) {
            String resultValueStr = ObjectUtil.objectToString(resultValue);
            String hashValue = getHashValue(resultValueStr, context);
            if(StringUtil.isNotEmpty(hashValue)) {
                resultValue = resultValueStr + '|' + hashValue;
            }

        }


        return resultValue;
    }

    /**
     * 获取对应的哈希值
     * @param desValue 脱敏值
     * @param context 上下文
     * @return 结果
     * @since 1.1.0
     */
    protected String getHashValue(String desValue,
                                  IContext context) {
        if(desValue == null
            || context == null) {
            return null;
        }

        final ISensitiveConfig sensitiveConfig = context.getSensitiveConfig();
        if(sensitiveConfig == null) {
            return null;
        }

        final IHash hash = sensitiveConfig.hash();
        return HashHelper.hash(hash, desValue);
    }

}
