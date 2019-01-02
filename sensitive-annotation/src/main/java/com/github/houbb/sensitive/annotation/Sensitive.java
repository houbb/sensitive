package com.github.houbb.sensitive.annotation;

import com.github.houbb.sensitive.api.ICondition;
import com.github.houbb.sensitive.api.IStrategy;
import com.github.houbb.sensitive.api.impl.ConditionAlwaysTrue;

import java.lang.annotation.*;

/**
 * 脱敏
 * 1. 所有的注解都要继承这个注解
 * 2. 如果一个字段上面有多个注解，则根据注解的顺序，依次执行。
 * @author binbin.hou
 * date 2018/12/29
 */
@Inherited
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Sensitive {

    /**
     * 注解生效的条件
     * @return 条件对应的实现类
     */
    Class<? extends ICondition> condition() default ConditionAlwaysTrue.class;

    /**
     * 执行的策略
     * @return 策略对应的类型
     */
    Class<? extends IStrategy> strategy();

}
