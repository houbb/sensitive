package com.github.houbb.sensitive.core.util.condition;

import com.github.houbb.heaven.util.lang.ObjectUtil;
import com.github.houbb.heaven.util.lang.reflect.ClassUtil;
import com.github.houbb.heaven.util.util.Optional;
import com.github.houbb.sensitive.annotation.metadata.SensitiveCondition;
import com.github.houbb.sensitive.api.ICondition;

import java.lang.annotation.Annotation;

/**
 * 脱敏条件工具类
 * @author binbin.hou
 * @since 0.0.6
 */
public final class SensitiveConditions {

    private SensitiveConditions(){}

    /**
     * 获取用户自定义条件
     *
     * @param annotations 字段上的注解
     * @return 对应的用户自定义条件
     * @since 0.0.6
     */
    public static Optional<ICondition> getConditionOpt(final Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            SensitiveCondition sensitiveCondition = annotation.annotationType().getAnnotation(SensitiveCondition.class);
            if (ObjectUtil.isNotNull(sensitiveCondition)) {
                Class<? extends ICondition> customClass = sensitiveCondition.value();
                ICondition condition =  ClassUtil.newInstance(customClass);
                return Optional.ofNullable(condition);
            }
        }
        return Optional.empty();
    }

}
