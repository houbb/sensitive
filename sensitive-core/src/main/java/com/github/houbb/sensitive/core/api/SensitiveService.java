package com.github.houbb.sensitive.core.api;

import com.github.houbb.sensitive.annotation.Sensitive;
import com.github.houbb.sensitive.annotation.SensitiveEntry;
import com.github.houbb.sensitive.api.ICondition;
import com.github.houbb.sensitive.api.ISensitive;
import com.github.houbb.sensitive.api.IStrategy;
import com.github.houbb.sensitive.core.api.context.SensitiveContext;
import com.github.houbb.sensitive.core.exception.SensitiveRuntimeException;
import com.github.houbb.sensitive.core.util.*;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 脱敏服务实现类
 *
 * [反射处理数组](https://blog.csdn.net/snakemoving/article/details/54287681)
 * @author binbin.hou
 * @since 0.0.1
 * date 2018/12/29
 */
public class SensitiveService<T> implements ISensitive<T> {

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public T desCopy(T object) {
        //1. 初始化对象
        final Class clazz = object.getClass();
        final SensitiveContext context = new SensitiveContext();

        //2. 深度复制对象
        final T copyObject = BeanUtil.deepCopy(object);
        context.setCurrentObject(copyObject);


        //3. 处理
        handleClassField(context, copyObject, clazz);
        return copyObject;
    }

    /**
     * 处理脱敏相关信息
     *
     * @param context    执行上下文
     * @param copyObject 拷贝的新对象
     * @param clazz      class 类型
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private void handleClassField(final SensitiveContext context,
                                  final Object copyObject,
                                  final Class clazz) {

        List<Field> fieldList = ClassUtil.getAllFieldList(clazz);
        context.addFieldList(fieldList);

        try {
            for (Field field : fieldList) {
                // 设置当前处理的字段
                final Class fieldTypeClass = field.getType();
                context.setCurrentField(field);

                // 处理 @SensitiveEntry 注解
                SensitiveEntry sensitiveEntry = field.getAnnotation(SensitiveEntry.class);
                if (ObjectUtil.isNotNull(sensitiveEntry)) {
                    if (ClassUtil.isJavaBeanClass(fieldTypeClass)) {
                        // 为普通 javabean 对象
                        final Object fieldNewObject = field.get(copyObject);
                        handleClassField(context, fieldNewObject, fieldTypeClass);
                    } else if (ClassUtil.isArrayClass(fieldTypeClass)) {
                        // 为数组类型
                        Object[] arrays = (Object[]) field.get(copyObject);
                        if (ArrayUtil.isNotEmpty(arrays)) {
                            Object firstArrayEntry = arrays[0];
                            final Class entryFieldClass = firstArrayEntry.getClass();

                            //1. 如果需要特殊处理，则循环特殊处理
                            if (needHandleEntryType(entryFieldClass)) {
                                for (Object arrayEntry : arrays) {
                                    handleClassField(context, arrayEntry, entryFieldClass);
                                }
                            } else {
                                //2, 基础值，直接循环设置即可
                                final int arrayLength = arrays.length;
                                Object newArray = Array.newInstance(entryFieldClass, arrayLength);
                                for(int i = 0; i < arrayLength; i++) {
                                    Object entry = arrays[i];
                                    Object result = handleSensitiveEntry(context, entry, field);
                                    Array.set(newArray, i, result);
                                }
                                field.set(copyObject, newArray);
                            }
                        }
                    } else if (ClassUtil.isCollectionClass(fieldTypeClass)) {
                        // Collection 接口的子类
                        final Collection<Object> entryCollection = (Collection<Object>) field.get(copyObject);
                        if(CollectionUtil.isNotEmpty(entryCollection)) {
                            Object firstCollectionEntry = entryCollection.iterator().next();
                            Class collectionEntryClass = firstCollectionEntry.getClass();

                            //1. 如果需要特殊处理，则循环特殊处理
                            if(needHandleEntryType(collectionEntryClass)) {
                                for(Object collectionEntry : entryCollection) {
                                    handleClassField(context, collectionEntry, collectionEntryClass);
                                }
                            } else {
                                //2, 基础值，直接循环设置即可
                                List<Object> newResultList = new ArrayList<>(entryCollection.size());
                                for(Object entry : entryCollection) {
                                    Object result = handleSensitiveEntry(context, entry, field);
                                    newResultList.add(result);
                                }
                                field.set(copyObject, newResultList);
                            }
                        }
                    } else {
                        // 1. 常见的基本类型，不做处理
                        // 2. 如果为 map，暂时不支持处理。后期可以考虑支持 value 的脱敏，或者 key 的脱敏
                        // 3. 其他
                        // 处理单个字段脱敏信息
                        handleSensitive(context, copyObject, field);
                    }
                } else {
                    handleSensitive(context, copyObject, field);
                }
            }

        } catch (IllegalAccessException e) {
            throw new SensitiveRuntimeException(e);
        }
    }

    /**
     * 处理需脱敏的单个对象
     *
     * @param context   上下文
     * @param entry 明细
     * @param field     字段信息
     * @return 处理后的信息
     * @since 0.0.2
     */
    private Object handleSensitiveEntry(final SensitiveContext context,
                                        final Object entry,
                                        final Field field) {
        try {
            //处理 @Sensitive
            Sensitive sensitive = field.getAnnotation(Sensitive.class);
            if(ObjectUtil.isNotNull(sensitive)) {
                Class<? extends ICondition> conditionClass = sensitive.condition();
                ICondition condition = conditionClass.newInstance();
                if (condition.valid(context)) {
                    Class<? extends IStrategy> strategyClass = sensitive.strategy();
                    IStrategy strategy = strategyClass.newInstance();
                    return strategy.des(entry, context);
                }
            }

            // 系统内置自定义注解的处理

            // 其他用户自定义注解的处理
            return entry;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new SensitiveRuntimeException(e);
        }
    }

    /**
     * 处理脱敏信息
     *
     * @param context    上下文
     * @param copyObject 复制的对象
     * @param field      当前字段
     * @since 0.0.2
     */
    private void handleSensitive(final SensitiveContext context,
                                 final Object copyObject,
                                 final Field field) {
        try {
            //处理 @Sensitive
            Sensitive sensitive = field.getAnnotation(Sensitive.class);
            if (sensitive != null) {
                Class<? extends ICondition> conditionClass = sensitive.condition();
                ICondition condition = conditionClass.newInstance();
                if (condition.valid(context)) {
                    Class<? extends IStrategy> strategyClass = sensitive.strategy();
                    IStrategy strategy = strategyClass.newInstance();
                    final Object originalFieldVal = field.get(copyObject);
                    final Object result = strategy.des(originalFieldVal, context);
                    field.set(copyObject, result);
                }
            }

            // 系统内置自定义注解的处理

            // 其他用户自定义注解的处理

        } catch (InstantiationException | IllegalAccessException e) {
            throw new SensitiveRuntimeException(e);
        }
    }

    /**
     * 需要特殊处理的列表/对象类型
     *
     * @param fieldTypeClass 字段类型
     * @return 是否
     * @since 0.0.2
     */
    private boolean needHandleEntryType(final Class fieldTypeClass) {
        if(ClassUtil.isBaseClass(fieldTypeClass)
            || ClassUtil.isMapClass(fieldTypeClass)) {
            return false;
        }

        if (ClassUtil.isJavaBeanClass(fieldTypeClass)
                || ClassUtil.isArrayClass(fieldTypeClass)
                || ClassUtil.isCollectionClass(fieldTypeClass)) {
            return true;
        }
        return false;
    }

}
