/*
 * Copyright (c)  2019. houbinbin Inc.
 * sensitive All rights reserved.
 */

package com.github.houbb.sensitive.api.metadata;

import com.github.houbb.sensitive.api.ICondition;

import java.lang.annotation.Annotation;

/**
 * 自定义脱敏条件接口
 *
 * 1. 当用户自定义的时候，可以允许用户不定义对应的策略，但是我们目前支持定义。
 * 如果用户定义了其他方法，我们不做处理。
 * @author binbin.hou
 * date 2019/01/16
 * @param <T> 参数类型
 * @since 0.0.4
 */
public interface ISensitiveCondition<T> extends ICondition {

    /**
     * 条件对应的策略
     * 1. 当为默认值时，则说明当前指定的所有策略都生效
     * 2. 如果指定了具体的值，则只有当前条件生效时，对应的策略才会生效
     * 3. 如果指定了多个条件注解，则按照注解的默认获取顺序，依次判断条件是否生效，依次指定生效条件的策略。
     * @return 对应的策略列表
     */
    Class<? extends Annotation>[] strategy();

}
