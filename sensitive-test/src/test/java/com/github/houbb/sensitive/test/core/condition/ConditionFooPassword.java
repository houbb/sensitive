package com.github.houbb.sensitive.test.core.condition;

import com.github.houbb.sensitive.api.ICondition;
import com.github.houbb.sensitive.api.IContext;

import java.lang.reflect.Field;

/**
 * 让这些 123456 的密码不进行脱敏
 * @author binbin.hou
 * date 2019/1/2
 * @since 0.0.1
 */
public class ConditionFooPassword implements ICondition {
    @Override
    public boolean valid(IContext context) {
        try {
            Field field = context.getCurrentField();
            final Object currentObj = context.getCurrentObject();
            final String name = (String) field.get(currentObj);
            return !name.equals("123456");
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
