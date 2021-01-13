package com.github.houbb.sensitive.annotation;

import java.lang.annotation.*;

@Inherited
@Documented
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SensitiveIgnore {
}
