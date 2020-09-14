package com.github.houbb.sensitive.test.annotation.custom;

import com.github.houbb.sensitive.annotation.SensitiveEntry;

import java.lang.annotation.*;

/**
 * 如果对象中属性为另外一个对象(集合)，则可以使用这个注解指定。(自定义)
 * <p>
 * 1. 如果属性为 Iterable 的子类集合，则当做列表处理，遍历其中的对象
 * 2. 如果是普通对象，则处理对象中的脱敏信息
 * 3. 如果是普通字段/MAP，则不做处理
 *
 * @author dev-sxl
 * date 2020-09-14
 * @since 0.0.11
 */
@Inherited
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@SensitiveEntry
public @interface SensitiveEntryCustom {
}
