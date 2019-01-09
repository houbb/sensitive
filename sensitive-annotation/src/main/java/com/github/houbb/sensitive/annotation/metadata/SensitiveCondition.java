package com.github.houbb.sensitive.annotation.metadata;

import com.github.houbb.sensitive.annotation.condition.SenstiveConditionAlwaysTrue;
import com.github.houbb.sensitive.api.ICondition;

import java.lang.annotation.*;

/**
 * 用于自定义 sensitive 注解
 * @since 0.0.2
 * @author binbin.hou
 * date 2019/1/9
 * @see com.github.houbb.sensitive.api.ICondition 条件
 */
@Inherited
@Documented
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SensitiveCondition {

    /**
     * 策略生效的条件
     * @return 对应的条件实现
     */
    Class<? extends ICondition> value();

}
