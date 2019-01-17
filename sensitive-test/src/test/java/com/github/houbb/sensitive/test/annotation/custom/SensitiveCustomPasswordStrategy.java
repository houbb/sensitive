package com.github.houbb.sensitive.test.annotation.custom;

import com.github.houbb.sensitive.annotation.metadata.SensitiveStrategy;
import com.github.houbb.sensitive.api.impl.SensitiveStrategyBuiltIn;
import com.github.houbb.sensitive.test.core.custom.CustomPasswordStrategy;

import java.lang.annotation.*;

/**
 * @author binbin.hou
 * date 2019/1/17
 * @since 0.0.4
 */
@Inherited
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@SensitiveStrategy(CustomPasswordStrategy.class)
public @interface SensitiveCustomPasswordStrategy {
}
