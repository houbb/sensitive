package com.github.houbb.sensitive.core.util.strategy;

import com.github.houbb.heaven.util.lang.ObjectUtil;
import com.github.houbb.heaven.util.lang.reflect.ClassUtil;
import com.github.houbb.heaven.util.util.Optional;
import com.github.houbb.sensitive.annotation.metadata.SensitiveStrategy;
import com.github.houbb.sensitive.annotation.strategy.*;
import com.github.houbb.sensitive.api.IStrategy;
import com.github.houbb.sensitive.api.impl.SensitiveStrategyBuiltIn;
import com.github.houbb.sensitive.core.api.strategory.*;
import com.github.houbb.sensitive.core.exception.SensitiveRuntimeException;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

/**
 * 系统中内置的策略映射
 * 1. 注解和实现之间映射
 * @author binbin.hou
 * date 2019/1/9
 * @since 0.0.2
 */
public final class SensitiveStrategyBuiltInUtil {

    private SensitiveStrategyBuiltInUtil(){}

    /**
     * 注解和实现策略的映射关系
     */
    private static final Map<Class<? extends Annotation>, IStrategy> MAP = new HashMap<>();

    static {
        MAP.put(SensitiveStrategyCardId.class, new StrategyCardId());
        MAP.put(SensitiveStrategyPassword.class, new StrategyPassword());
        MAP.put(SensitiveStrategyPhone.class, new StrategyPhone());
        MAP.put(SensitiveStrategyChineseName.class, new StrategyChineseName());
        MAP.put(SensitiveStrategyEmail.class, new StrategyEmail());
    }

    /**
     * 获取对应的系统内置实现
     * @param annotationClass 注解实现类
     * @return 对应的实现方式
     */
    public static IStrategy require(final Class<? extends Annotation> annotationClass) {
        IStrategy strategy = MAP.get(annotationClass);
        if(ObjectUtil.isNull(strategy)) {
            throw new SensitiveRuntimeException("不支持的系统内置方法，用户请勿在自定义注解中使用[SensitiveStrategyBuiltIn]!");
        }
        return strategy;
    }

    /**
     * 获取策略
     *
     * @param annotations 字段对应注解
     * @return 策略
     * @since 0.0.6
     */
    public static Optional<IStrategy> getStrategyOpt(final Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            SensitiveStrategy sensitiveStrategy = annotation.annotationType().getAnnotation(SensitiveStrategy.class);
            if (ObjectUtil.isNotNull(sensitiveStrategy)) {
                Class<? extends IStrategy> clazz = sensitiveStrategy.value();
                IStrategy strategy = null;
                if (SensitiveStrategyBuiltIn.class.equals(clazz)) {
                    strategy = SensitiveStrategyBuiltInUtil.require(annotation.annotationType());
                } else {
                    strategy = ClassUtil.newInstance(clazz);
                }
                return Optional.ofNullable(strategy);
            }
        }
        return Optional.empty();
    }

}
