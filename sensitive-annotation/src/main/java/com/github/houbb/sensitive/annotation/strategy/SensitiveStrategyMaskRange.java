package com.github.houbb.sensitive.annotation.strategy;

import com.github.houbb.sensitive.annotation.metadata.SensitiveStrategy;
import com.github.houbb.sensitive.api.impl.SensitiveStrategyBuiltIn;

import java.lang.annotation.*;

/**
 * 范围脱敏注解
 * @author binbin.hou
 * date 2019/1/9
 * @since 0.0.2
 */
@Inherited
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@SensitiveStrategy(SensitiveStrategyBuiltIn.class)
public @interface SensitiveStrategyMaskRange {

    /**
     * 前面的明文长度
     * @return 长度
     */
    int beforeLen() default 1;

    /**
     * 后面的明文长度
     * @return 长度
     */
    int afterLen() default 1;

}
