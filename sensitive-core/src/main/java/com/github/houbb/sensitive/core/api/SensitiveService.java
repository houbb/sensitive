package com.github.houbb.sensitive.core.api;

import com.github.houbb.sensitive.annotation.Sensitive;
import com.github.houbb.sensitive.api.ICondition;
import com.github.houbb.sensitive.api.ISensitive;
import com.github.houbb.sensitive.api.IStrategy;
import com.github.houbb.sensitive.core.api.context.SensitiveContext;
import com.github.houbb.sensitive.core.util.BeanUtil;
import com.github.houbb.sensitive.core.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.List;

/**
 * 脱敏服务实现类
 *
 * @author binbin.hou
 * @date 2018/12/29
 */
public class SensitiveService<T> implements ISensitive<T> {

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public T desCopy(T object) {
        try {
            //1. 初始化对象
            final Class clazz = object.getClass();
            final T newObject = (T) clazz.newInstance();

            //2. 对象的信息处理
            BeanUtil.copyProperties(object, newObject);
            List<Field> fieldList = ReflectionUtil.getAllFieldList(clazz);

            //2.1 上下文的构造
            SensitiveContext context = new SensitiveContext();
            context.setAllFieldList(fieldList);
            context.setCurrentObject(newObject);

            for (Field field : fieldList) {
                context.setCurrentField(field);
                Sensitive sensitive = field.getAnnotation(Sensitive.class);
                if (sensitive != null) {
                    Class<? extends ICondition> conditionClass = sensitive.condition();
                    ICondition condition = conditionClass.newInstance();
                    if (condition.valid(context)) {
                        Class<? extends IStrategy> strategyClass = sensitive.strategy();
                        IStrategy strategy = strategyClass.newInstance();
                        final Object originalFieldVal = field.get(newObject);
                        final Object result = strategy.des(originalFieldVal, context);
                        field.set(newObject, result);
                    }
                }
            }

            return newObject;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
