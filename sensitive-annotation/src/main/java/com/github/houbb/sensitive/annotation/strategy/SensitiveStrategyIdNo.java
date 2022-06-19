package com.github.houbb.sensitive.annotation.strategy;

import com.github.houbb.sensitive.annotation.metadata.SensitiveStrategy;
import com.github.houbb.sensitive.api.impl.SensitiveStrategyBuiltIn;

import java.lang.annotation.*;

/**
 * 中国身份证号脱敏注解
 * @author binbin.hou
 * @since 0.0.15
 */
@Inherited
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@SensitiveStrategy(SensitiveStrategyBuiltIn.class)
public @interface SensitiveStrategyIdNo {
}
