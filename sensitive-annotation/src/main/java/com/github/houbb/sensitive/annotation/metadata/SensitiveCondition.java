package com.github.houbb.sensitive.annotation.metadata;

import com.github.houbb.sensitive.api.metadata.ISensitiveCondition;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于自定义策略生效条件的注解
 * @since 0.0.4
 * @author binbin.hou
 * date 2019/1/9
<<<<<<< 3b95423ea655115774d719535a99699e36ac3731
=======
 * @see com.github.houbb.sensitive.api.metadata.ISensitiveCondition 条件接口
>>>>>>> [Feature] 添加自定义注解和接口
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
    Class<? extends ISensitiveCondition> value();

}
