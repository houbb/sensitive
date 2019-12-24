package com.github.houbb.sensitive.core.support.filter;

import com.alibaba.fastjson.serializer.BeanContext;
import com.alibaba.fastjson.serializer.ContextValueFilter;
import com.github.houbb.heaven.annotation.ThreadSafe;
import com.github.houbb.heaven.support.cache.impl.ClassFieldListCache;
import com.github.houbb.heaven.util.lang.ObjectUtil;
import com.github.houbb.heaven.util.lang.reflect.ClassTypeUtil;
import com.github.houbb.heaven.util.util.ArrayUtil;
import com.github.houbb.heaven.util.util.CollectionUtil;
import com.github.houbb.heaven.util.util.Optional;
import com.github.houbb.sensitive.annotation.Sensitive;
import com.github.houbb.sensitive.annotation.SensitiveEntry;
import com.github.houbb.sensitive.api.ICondition;
import com.github.houbb.sensitive.api.IStrategy;
import com.github.houbb.sensitive.core.api.context.SensitiveContext;
import com.github.houbb.sensitive.core.exception.SensitiveRuntimeException;
import com.github.houbb.sensitive.core.util.condition.SensitiveConditions;
import com.github.houbb.sensitive.core.util.strategy.SensitiveStrategyBuiltInUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 默认的上下文过滤器
 * (1) 和原来的对象有区别。因为是根据 JSON 的序列化来构建的。
 * 所以 {@link SensitiveEntry} 放在对象时，则不用特殊处理。
 * 只需要处理 集合、数组集合。
 * （2）0.0.6 这一期暂时不做代码的优化。保证功能性的正确。
 *
 *  注意：
 * 和 {@link com.github.houbb.sensitive.core.api.SensitiveUtil#desCopy(Object)} 的区别
 * 因为 FastJSON 本身的转换问题，如果对象中存储的是集合对象列表，会导致显示不是信息本身。
 * @author binbin.hou
 * @since 0.0.6
 */
@ThreadSafe
public class DefaultContextValueFilter implements ContextValueFilter {

    /**
     * 脱敏上下文
     */
    private final SensitiveContext sensitiveContext;

    public DefaultContextValueFilter(SensitiveContext context) {
        this.sensitiveContext = context;
    }

    @Override
    public Object process(BeanContext context, Object object, String name, Object value) {
        // 对象为 MAP 的时候，FastJson map 对应的 context 为 NULL
        if(ObjectUtil.isNull(context)) {
            return value;
        }

        // 信息初始化
        final Field field = context.getField();
        final Class clazz = context.getBeanClass();
        final List<Field> fieldList = ClassFieldListCache.getInstance().get(clazz);
        sensitiveContext.setCurrentField(field);
        sensitiveContext.setCurrentObject(object);
        sensitiveContext.setBeanClass(clazz);
        sensitiveContext.setAllFieldList(fieldList);

        // 这里将缺少对于列表/集合/数组 的处理。可以单独实现。
        // 设置当前处理的字段
        SensitiveEntry sensitiveEntry = field.getAnnotation(SensitiveEntry.class);
        if(ObjectUtil.isNull(sensitiveEntry)) {
            sensitiveContext.setEntry(value);
            return handleSensitive(sensitiveContext, field);
        }

        //2. 处理 @SensitiveEntry 注解
        final Class fieldTypeClass = field.getType();
        if (ClassTypeUtil.isJavaBean(fieldTypeClass)) {
            //不作处理，因为 json 本身就会进行递归处理
            return value;
        }
        if(ClassTypeUtil.isMap(fieldTypeClass)) {
            return value;
        }

        if(ClassTypeUtil.isArray(fieldTypeClass)) {
            // 为数组类型
            Object[] arrays = (Object[]) value;
            if (ArrayUtil.isNotEmpty(arrays)) {
                Object firstArrayEntry = ArrayUtil.firstNotNullElem(arrays).get();
                final Class entryFieldClass = firstArrayEntry.getClass();

                if(isBaseType(entryFieldClass)) {
                    //2, 基础值，直接循环设置即可
                    final int arrayLength = arrays.length;
                    Object newArray = Array.newInstance(entryFieldClass, arrayLength);
                    for (int i = 0; i < arrayLength; i++) {
                        Object entry = arrays[i];
                        sensitiveContext.setEntry(entry);
                        Object result = handleSensitive(sensitiveContext, field);
                        Array.set(newArray, i, result);
                    }

                    return newArray;
                }
            }
        }
        if(ClassTypeUtil.isCollection(fieldTypeClass)) {
            // Collection 接口的子类
            final Collection<Object> entryCollection = (Collection<Object>) value;
            if (CollectionUtil.isNotEmpty(entryCollection)) {
                Object firstCollectionEntry = CollectionUtil.firstNotNullElem(entryCollection).get();

                if(isBaseType(firstCollectionEntry.getClass())) {
                    //2, 基础值，直接循环设置即可
                    List<Object> newResultList = new ArrayList<>(entryCollection.size());
                    for (Object entry : entryCollection) {
                        sensitiveContext.setEntry(entry);
                        Object result = handleSensitive(sensitiveContext, field);
                        newResultList.add(result);
                    }
                    return newResultList;
                }
            }
        }

        // 默认返回原来的值
        return value;
    }

    /**
     * 处理脱敏信息
     *
     * @param context    上下文
     * @param field      当前字段
     * @since 0.0.6
     */
    private Object handleSensitive(final SensitiveContext context,
                                 final Field field) {
        try {
            // 原始字段值
            final Object originalFieldVal = context.getEntry();

            //处理 @Sensitive
            Sensitive sensitive = field.getAnnotation(Sensitive.class);
            if (ObjectUtil.isNotNull(sensitive)) {
                Class<? extends ICondition> conditionClass = sensitive.condition();
                ICondition condition = conditionClass.newInstance();
                if (condition.valid(context)) {
                    Class<? extends IStrategy> strategyClass = sensitive.strategy();
                    IStrategy strategy = strategyClass.newInstance();
                    sensitiveContext.setEntry(null);
                    return strategy.des(originalFieldVal, context);
                }
            }

            // 系统内置自定义注解的处理,获取所有的注解
            Annotation[] annotations = field.getAnnotations();
            if (ArrayUtil.isNotEmpty(annotations)) {
                Optional<ICondition> conditionOptional = SensitiveConditions.getConditionOpt(annotations);
                if (conditionOptional.isNotPresent()
                        || conditionOptional.get().valid(context)) {
                    final Optional<IStrategy> strategyOptional = SensitiveStrategyBuiltInUtil.getStrategyOpt(annotations);
                    if (strategyOptional.isPresent()) {
                        sensitiveContext.setEntry(null);
                        return strategyOptional.get().des(originalFieldVal, context);
                    }
                }
            }

            sensitiveContext.setEntry(null);
            return originalFieldVal;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new SensitiveRuntimeException(e);
        }
    }

    /**
     * 特殊类型
     * （1）map
     * （2）对象
     * （3）集合/数组
     * @param fieldTypeClass 字段类型
     * @return 是否
     * @since 0.0.6
     */
    private boolean isBaseType(final Class fieldTypeClass) {
        if (ClassTypeUtil.isBase(fieldTypeClass)) {
            return true;
        }

        if (ClassTypeUtil.isJavaBean(fieldTypeClass)
                || ClassTypeUtil.isArray(fieldTypeClass)
                || ClassTypeUtil.isCollection(fieldTypeClass)
                || ClassTypeUtil.isMap(fieldTypeClass)) {
            return false;
        }
        return true;
    }

}
