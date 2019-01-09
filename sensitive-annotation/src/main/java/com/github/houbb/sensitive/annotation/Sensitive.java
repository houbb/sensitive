package com.github.houbb.sensitive.annotation;

import com.github.houbb.sensitive.api.ICondition;
import com.github.houbb.sensitive.api.IStrategy;
import com.github.houbb.sensitive.api.impl.ConditionAlwaysTrue;

import java.lang.annotation.*;

/**
 * 脱敏
 * 1. 所有的注解都要继承这个注解
 * 2. 如果一个字段上面有多个注解，则根据注解的顺序，依次执行。
 *
 * 设计的考虑：
 * 本来想过将生效条件单独抽离为一个注解，这样可以达到条件注解的复用。
 * 但是有一个缺点，当指定多个策略时，条件的注解就会太宽泛，无法保证精细到每一个策略生效的场景。
 *
 * 平衡的方式：
 * 在 condition 注解中，可以指定策略。默认是全部，如果指定，则只针对其中的某个策略生效。
 * @author binbin.hou
 * date 2018/12/29
 * @since 0.0.1
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
