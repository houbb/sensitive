package com.github.houbb.sensitive.core.util.strategy;

import com.github.houbb.sensitive.annotation.condition.SenstiveConditionAlwaysTrue;
import com.github.houbb.sensitive.annotation.strategy.*;
import com.github.houbb.sensitive.api.IStrategy;
import com.github.houbb.sensitive.core.api.strategory.*;
import com.github.houbb.sensitive.core.exception.SenstiveRuntimeException;
import com.github.houbb.sensitive.core.util.ObjectUtil;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

/**
 * 系统中内置的策略映射
 * 1. 注解和实现之间映射
 * @author binbin.hou
 * date 2019/1/9
 * @since 0.0.1
 */
public final class SensitiveStrategyBuiltInUtil {

    private SensitiveStrategyBuiltInUtil(){}

    /**
     * 注解和实现策略的映射关系
     */
    private static final Map<Class<? extends Annotation>, IStrategy> MAP = new HashMap<>();

    static {
        MAP.put(SenstiveStrategyCardId.class, new StrategyCardId());
        MAP.put(SenstiveStrategyPassword.class, new StrategyPassword());
        MAP.put(SenstiveStrategyPhone.class, new StrategyPhone());
        MAP.put(SenstiveStrategyChineseName.class, new StrategyChineseName());
        MAP.put(SenstiveStrategyEmail.class, new StrategyEmail());
    }

    /**
     * 获取对应的系统内置实现
     * @param annotationClass 注解实现类
     * @return 对应的实现方式
     */
    public static IStrategy require(final Class<? extends Annotation> annotationClass) {
        IStrategy strategy = MAP.get(annotationClass);
        if(ObjectUtil.isNull(strategy)) {
            throw new SenstiveRuntimeException("不支持的系统内置方法，用户请勿在自定义注解中使用[SensitiveStrategyBuiltIn]!");
        }
        return strategy;
    }

}
