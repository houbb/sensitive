package com.github.houbb.sensitive.annotation.metadata;

import com.github.houbb.sensitive.api.IStrategy;

import java.lang.annotation.*;

/**
 * 用于自定义 sensitive 注解
 * @since 0.0.2
 * @author binbin.hou
 * date 2019/1/9
 * @see com.github.houbb.sensitive.api.IStrategy 策略信息
 */
@Inherited
@Documented
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SensitiveStrategy {

    /**
     * 脱敏的策略实现
     * @return 策略实现类信息
     */
    Class<? extends IStrategy> value();

}
